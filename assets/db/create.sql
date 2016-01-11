CREATE TABLE User (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	username TEXT NOT NULL,
	password TEXT NOT NULL,
	university TEXT NOT NULL,
	userkey TEXT NOT NULL
);

CREATE TABLE SubjectFilter (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	subjectid TEXT NOT NULL
);

CREATE TABLE Subjects (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	subjectid TEXT NOT NULL,
	university TEXT NOT NULL,
	name TEXT NOT NULL,
	taken TEXT,
	link TEXT,
	lvanr TEXT,
	type TEXT,
	semester TEXT,
	ects REAL,
	hours REAL
);

CREATE TABLE Task (
	id INTEGER PRIMARY KEY AUTOINCREMENT,
	title TEXT NOT NULL,
	description TEXT NOT NULL,
	color TEXT,
	deadlineDate INTEGER,
	imagePath TEXT
);
