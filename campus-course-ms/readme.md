# campus-course-ms

### Rest Calls
```
GET  http://127.0.0.1:8080/courses
POST http://127.0.0.1:8080/courses
GET  http://127.0.0.1:8080/courses/1
POST http://127.0.0.1:8080/courses/1/topics
GET  http://127.0.0.1:8080/courses/1/topics
GET  http://127.0.0.1:8080/courses/deleteall

GET  http://127.0.0.1:8080/topics
POST http://127.0.0.1:8080/topics
GET  http://127.0.0.1:8080/topics/1
GET  http://127.0.0.1:8080/topics/deleteall
```
```
Course Json
{
	"id": "1",
	"name": "Java",
	"description": "Java course"
}

Topic Json
http://127.0.0.1:8080/courses/1/topic
{
	"id": "1",
	"name": "Array",
	"description": "Array desc"
}

Post Header
[{"key":"Content-Type","value":"application/json","description":""}]

```

# See
* [Campus-MS] Parent directory

[Campus-MS]: <https://github.com/ermalaliraj/campus-ms>