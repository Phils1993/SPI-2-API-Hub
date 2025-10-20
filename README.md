# SPI-2-API-Hub


### Structure of program
We have Day, Exercise, and Week entities, but we want to include extra information like sets, reps, duration, etc. in the API. That means that a simply ManyToMany relationship between Day and Exercise wouldn't work, as it would only store the relationship, but not the extra data (can't attach additional information to a plain ManyToMany).

The solution is to create a new Entity, a Join Entity. We create the Entity DayExercise, which is a Join Entity that replaces the ManyToMany join table.

So the table would look like this:

````sql
| day_id | exercise_id | sets | reps | duration_seconds |
| ------ | ----------- | ---- | ---- | ---------------- |
| 1      | 101         | 4    | 12   | 0                |
| 1      | 102         | 3    | 10   | 0                |
| 1      | 103         | 3    | 0    | 60               |
````

Which now means each link between Day and Exercise can hold real data.

We also make a composite key, which is DayExerciseKey. It identifies a unique link between a specific Day and Exercise. It combines both foreign keys: day_id + exercise_id and together those keys say: "This specific exercise belongs to this specific day, with these sets/reps/duration values."

### ⚙️ 2. How JPA understands the relationship
````plaintext
Day.java

Has @OneToMany(mappedBy = "day")

JPA knows: one Day → many DayExercise entries
````

````plaintext
Exercise.java

Has @OneToMany(mappedBy = "exercise")

JPA knows: one Exercise → many DayExercise entries
````

````plaintext
DayExercise.java

Has @ManyToOne to both Day and Exercise,
and uses @MapsId("dayId") and @MapsId("exerciseId")
so that both IDs are part of the primary key.
````

Result:
JPA can navigate relationships like this:

day.getDayExercises() → gives you all exercises for that day

exercise.getDayExercises() → gives you all days where that exercise appears

And it keeps everything consistent in the database.

### JsonReference and JsonBackReference
The reason we use those is to avoid recursive calls and ultimately stackoverflow errors.

### 🧠 The Context

Your relationships look like this:

Week ↔ Day

One Week can have many Days

Each Day belongs to one Week

Day ↔ DayExercise

One Day can have many DayExercises

Each DayExercise belongs to one Day

Both relationships are bidirectional — meaning:

From Week, you can navigate to its Days

From each Day, you can navigate back to its Week

From each Day, you can navigate to its DayExercises

From each DayExercise, you can navigate back to its Day

### ⚙️ The JSON Problem

When you return a Week from a REST API (e.g., /api/week/1), Jackson tries to serialize everything it can access.

Without @JsonManagedReference / @JsonBackReference

It would look like this in pseudocode:

Serialize Week

It sees week.getDays() → starts serializing all Days

Each Day has day.getWeek() → Jackson goes back to serialize Week

That Week again has getDays() → and now we’re in an infinite loop 🔁

💥 Boom → StackOverflowError / Infinite recursion