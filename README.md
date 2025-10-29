# employee-ld-platform
The Employee Learning and Career Development Platform is a comprehensive solution designed to facilitate employee growth through structured learning and career progression.

The system supports three main roles:
- **Employee**: Submits learnings, tracks progress, builds their career package.
- **Manager**: Reviews and approves/rejects submissions from direct reports, provides feedback.
- **Admin (HR)**: Manages platform configuration such as levels, points, and career package structure.

The solution is built with:
- **Backend**: Spring Boot microservices (Java), using PostgreSQL
- **Frontend**: Angular single-page application
- **Architecture**: API Gateway + microservices
- **Auth**: JWT-based authentication (planned)

---

## High-Level Features

### Shared (All Users)
- Login / Sign-Up
- Home dashboard:
  - Learning scoreboard (top learners of the quarter)
  - Journey map / progression levels
  - Notifications

### Library (Learning / Wiki / Blogs)
- Employees can submit learnings, wikis, and blog content
- Managers can approve/reject and leave comments
- Submission status tracking (pending / approved / changes requested)

### Career Package
- Employees build and upload their career package for their role/title
- Managers review and comment/approve
- Admins define/edit the career package structure and required sections

---
