# LearnEasy

## Introduction

This repository contains the backend, the frontend and the scientific paper in German of **LearnEasy**. The project is a learning application for schools that can be used by teachers and pupils. It has a special focus on accessibility and platform independence and does not store any personal data.
- Anonymous accounts: Each user requires a registration code, which is provided in the staff room in case of a teacher and by a teacher in case of a student. When registering, only this registration code is required in order to create an account with the role teacher or student and an automatically and randomly generated username that serves as a password for future logins. Provided the user keeps the username secret, the account is completely anonymous.
- Subjects: Users with an account with the role teacher can create subjects. When doing so, they receive a registration code that they can pass on to their students. Such a registration code can be used by students who already have a LearnEasy account to join the respective subject. On the other hand, the registration code can also be used by students who are not yet registered to create an account, while also joining the respective subject.
- Exercises: For each of their subjects, teachers can create exercises. For this purpose, the respective schoolbook must be specified when creating the subject. LearnEasy provides predefined exercise templates for the individual chapters of a schoolbook, allowing teachers to create exercises based on these templates by configuring just a few settings themselves. Students can do the exercises for their subjects and receive a stamp after successfully completing an exercise for the first time.

The paper describes the specification, design, implementation, testing and validation of LearnEasy in detail and also outlines the step-by-step further development. A first prototype of LearnEasy was developed as a RESTful web service in Java with Spring Boot in the backend and as a progressive web app with TypeScript and Vue.js in the frontend. The current prototype contains exercise templates for language lessons, but Learneasy is designed to be very open and can therefore be used for other subjects at any time without the need for any adjustments. The vocabulary exercises are based on the schoolbook “À plus ! - French as a 1st and 2nd foreign language - Bavaria - 2017 edition” by Cornelsen Verlag. In addition, the icons used come from https://www.flaticon.com/ and the audio files from https://ttsmp3.com.

The project was created during our Master‘s programme at Hof University of Applied Sciences in the lecture „Hybrid apps“.
<br>
<br>

## How to start
You can start the first prototype with the command `docker compose up` in the top-level repository folder
<br>
<br>

## Developers
- [Steven Solleder](https://github.com/stevensolleder)
- [Isabell Waas](https://github.com/isabellwaas)
<br>

## Get in contact
Feel free to get in contact and share your experience with **LearnEasy**. Bug reports are also very appreciated.
