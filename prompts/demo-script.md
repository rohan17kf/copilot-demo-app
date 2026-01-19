# GitHub Copilot Demo Script

## Phase 1: Project Scaffolding & Exploration

- **Goal**: Initialize projects for .NET, Java, Python, and React, and understand their structure.
- **Setup**: Start with an empty folder or clean workspace.

### 1. Create Projects

- **.NET Web API**:
  - Prompt: "Create a new .NET 8 Web API project named 'backend-dotnet' in the current directory. Include OpenAPI support."
- **Java Spring Boot**:
  - Prompt: "Create a Spring Boot 3.5.9 project named 'backend-java' with 'web' and 'actuator' dependencies with Maven wrapper"
- **Python FastAPI (uv)**:
  - Prompt: "Initialize a new Python project named 'backend-python' using 'uv'. Add 'fastapi' and 'uvicorn' as dependencies."
- **Frontend (React/Vite)**:
  - Prompt: "Create a new React project named 'frontend-react' using Vite with TypeScript."

### 2. Run & Verify

- **Execution Instructions**:
  - Prompt: "@workspace How do I run each of the backend services and the frontend application? Provide the terminal commands for each."

## Phase 2: Requirements & Planning

- **Goal**: Capture functional and non-functional requirements, break them into JIRA-friendly work items (Epics → User Stories → Subtasks), and automate the creation of JIRA issues via MCP.
- **Branch**: `feature/requirements-planning`
- **Tasks**:
  - Create `docs/REQUIREMENTS.md` listing **functional** requirements (Product CRUD, search, validation, pagination, sorting/filtering) and **non-functional** requirements (performance targets, security, logging, monitoring, scalability, SLAs), plus clear acceptance criteria and basic test cases.
    - Add `docs/JIRA_BREAKDOWN.md` mapping each requirement to JIRA **Epics**, **User Stories**, and **Subtasks**, with _story points_, _priority_, and _acceptance criteria_.
    - Create `prompts/create_issues.md` examples and instructions with sample MCP prompt sequences to create Epics, Stories, and Subtasks programmatically, including linking (Epic → Story → Subtask).
- **Prompts**:
  - "Create `docs/REQUIREMENTS.md` with functional and non-functional requirements for the Product feature and acceptance criteria."
    - "Break the REQUIREMENTS into JIRA Epics → User Stories → Subtasks and produce `docs/JIRA_BREAKDOWN.md` with story points and acceptance criteria."
      - "Refer #docs/JIRA_BREAKDOWM.md and create a JIRA user stories with sub tasks and acceptance criteria for epic named '{EPIC_NAME}' in project '{JIRA_PROJECT_NAME}', add Stories linked to that Epic and sub tasks linked to stories"

## Phase 3: Architecture & Design

- **Goal**: Define system architecture, API contracts, and design artifacts required to safely implement the Product feature.
- **Branch**: `feature/architecture-design`
- **Tasks**:
  - Write `architecture/ARCHITECTURE.md` describing high-level components (frontend, API, backend services, data stores), data flow, and a deployment diagram (ASCII or Mermaid).
  - Add `architecture/DISCOVERY.md` capturing findings from workspace exploration (stack, key dependencies, notable configs) and link it from `ARCHITECTURE.md`.
  - Add an ADR at `docs/adr/0001-product-api.md` documenting major decisions (API contract format, auth approach, data model choices).
  - Produce an OpenAPI file `api/openapi-products.yaml` defining Product endpoints and models (GET /products, POST /products, PUT /products/{id}, DELETE /products/{id}).
  - Define interface contracts and provide example request/response payloads used for implementation and tests.
- **Prompts**:
  - "Generate `ARCHITECTURE.md` with components, a Mermaid diagram, and the `Product` data model."
  - "Create `api/openapi-products.yaml` that defines product endpoints and example request/response payloads."
  - "Add `architecture/DISCOVERY.md` by running discovery prompts: '@workspace Explain the project structure of this workspace. What tech stacks are used?', 'Open `backend-python/pyproject.toml` and list dependencies and their purpose', 'Open `backend-dotnet/backend-dotnet.csproj` and explain the target framework and packages used.'"

## Phase 4: CRUD Implementation

- **Goal**: Implement Product CRUD in Java, .NET, and Python.
- **Branch**: `feature/crud-operations`
- **Java**:
  - Open `BackendJavaApplication.java` (or create `ProductController.java`).
  - Prompt: "Create a `Product` record with fields: id, name, price. Then create a `ProductController` with a static list to store products and implement GET and POST endpoints."
- **.NET**:
  - Open `Program.cs`.
  - Prompt: "Define a `Product` record. Then, map a group of endpoints under `/products` to handle CRUD operations using an in-memory list."
- **Python**:
  - Open `main.py`.
  - Prompt: "Define a `Product` Pydantic model. Implement `GET /products` and `POST /products` endpoints using FastAPI and a global list."
- **Frontend (frontend-react)**:
  - Open `frontend-react/src/App.jsx`.
  - Prompt: "Create a functional component that fetches products from `http://localhost:8080/products` using `useEffect` and displays them in a list. Handle loading and error states."

## Phase 5: Testing & Code Review

- **Goal**: Generate tests, debug, and review code.
- **Branch**: `feature/tests-and-review`
- **Java**:
  - Open `ProductController.java`.
  - Prompt: "Generate JUnit 5 tests for the `getAllProducts` method."
- **.NET**:
  - Open `Program.cs`.
  - Prompt: "Generate xUnit tests for the POST endpoint. Assume using `Microsoft.AspNetCore.Mvc.Testing`."
- **Python**:
  - Open `main.py`.
  - Prompt: "Write pytest unit tests for the `create_product` endpoint, checking for valid and invalid input."
- **Code Review**:
  - Select a block of code in any file.
  - Prompt: "Review this code for potential performance issues or edge cases."
- **Bug Fix**:
  - Introduce a bug (e.g., remove a null check).
  - Prompt: "@workspace /fix Fix the bug in the selected code."

## Phase 6: Documentation

- **Goal**: Generate code documentation and project docs.
- **Branch**: `feature/documentation`
- **Inline Documentation**:
  - Open `backend-python/main.py`.
  - Prompt: "Add docstrings to all functions in this file following Google style guide."
- **API Documentation**:
  - Open `backend-dotnet/Program.cs`.
  - Prompt: "How do I enable Swagger/OpenAPI UI for this project?"
- **Project Documentation**:
  - Open `README.md`.
  - Prompt: "@workspace Write a comprehensive README.md that explains the project structure, how to run each backend service, and how to start the frontend."

## Phase 7: DevOps

- **Goal**: Containerize and Automate.
- **Branch**: `feature/devops`- **Docker**:
  - Action: Create `Dockerfile` in `backend-python`.
  - Prompt: "Generate a multi-stage Dockerfile for this FastAPI application using a lightweight python image."
- **Docker Compose**:
  - Action: Create `docker-compose.yml` in the root.
  - Prompt: "@workspace Generate a docker-compose.yml file to run all three backends and the frontend service. Map ports 8080, 5000, 8000, and 3000 respectively."
- **CI/CD**:
  - Action: Create `.github/workflows/ci.yml`.
  - Prompt: "Create a GitHub Actions workflow that runs tests for the Python and .NET projects on every push to main."
