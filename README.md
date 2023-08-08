# Recipe Management API

The Recipe Management API is a Java application that allows users to manage favorite recipes. It provides Restful endpoints for adding, updating, removing, and fetching recipes. Additionally, users can filter available recipes based on various criteria.

## Table of Contents

- [Features](#features)
- [Endpoints](#endpoints)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Swagger Documentation](#swagger-documentation)

## Features

The Recipe Management API provides the following features:

1. Add a new recipe.
2. Update an existing recipe.
3. Remove a recipe.
4. Fetch a recipe by ID.
5. Fetch all recipes.
6. Filter recipes based on the following criteria:
    - Whether or not the dish is vegetarian.
    - The number of servings.
    - Recipe name search (either include or exclude).
    - Specific ingredients (either include or exclude).
    - Text search within the instructions (either include or exclude).

Note: the endpoint /recipes/filter, and the query parameters are used to specify the filtering criteria. we can include or exclude any combination of these parameters to tailor search.

vegetarian (boolean): Filter recipes by whether they are vegetarian or not.

servings (integer): Filter recipes by the number of servings.

includeIngredients (list of strings): Include recipes that contain any of the specified ingredients.

excludeIngredients (list of strings): Exclude recipes that contain any of the specified ingredients.

searchText (string): Filter recipes based on a text search within the instructions.

excludeInstructions (string): Exclude recipes with instructions that contain the specified text.

name (string): Filter recipes based on a text search within the recipe name.

excludeName (string): Exclude recipes with names that contain the specified text.

## Endpoints

The following are the available endpoints:

- `GET /recipes`: Fetch all recipes.
- `GET /recipes/{id}`: Fetch a recipe by ID.
- `POST /recipes`: Add a new recipe.
- `PUT /recipes/{id}`: Update an existing recipe.
- `DELETE /recipes/{id}`: Remove a recipe.
- `GET /recipes/filter`: Filter recipes based on criteria (query parameters).

### More Info On Filter Recipes End Point

Filter available recipes based on criteria such as vegetarian, servings, ingredients, instructions, and name.

- **URL**: `/recipes/filter`
- **Method**: GET
- **Request Parameters**:
   - `vegetarian` (boolean): Filter recipes by whether they are vegetarian or not.
   - `servings` (integer): Filter recipes by the number of servings.
   - `includeIngredients` (list of strings): Include recipes that contain any of the specified ingredients.
   - `excludeIngredients` (list of strings): Exclude recipes that contain any of the specified ingredients.
   - `searchText` (string): Filter recipes based on a text search within the instructions.
   - `excludeInstructions` (string): Exclude recipes with instructions that contain the specified text.
   - `name` (string): Filter recipes based on a text search within the recipe name.
   - `excludeName` (string): Exclude recipes with names that contain the specified text.
- **Response**: A list of recipes that match the specified criteria.

**Example Request:**
GET /recipes/filter?vegetarian=true&servings=4&includeIngredients=Tomatoes&excludeIngredients=Salmon&searchText=oven&excludeInstructions=fry&name=Pasta&excludeName=Spaghetti

**Example Response:**
[
{
"id": 1,
"name": "Delicious Pasta",
"isVegetarian": true,
"servings": 4,
"ingredients": ["Pasta", "Tomatoes", "Onions", ...],
"instructions": "..."
},
{
"id": 2,
"name": "Veggie Stir-Fry",
"isVegetarian": true,
"servings": 3,
"ingredients": ["Broccoli", "Carrots", "Peppers", ...],
"instructions": "..."
},
...
]

## Technologies Used

The Recipe Management API is built using the following technologies:

- Java
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven

## Getting Started

To run the Recipe Management API on your local machine, follow these steps:

1. Clone the repository: `git clone https://github.com/adityapnv/recipe-management-service.git`
2. Navigate to the project directory: `cd recipe-management`
3. Build the application: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

The API will start running at `http://localhost:8081`.

## Usage

Once the application is up and running, you can use tools like `curl`, `Postman`, or any REST client to interact with the API.

To test the endpoints, refer to the [Endpoints](#endpoints) section for the available API endpoints and their descriptions.

## Swagger Documentation

The API documentation is available using Swagger. You can access the Swagger UI to explore the API endpoints and interact with them directly.

To access the Swagger documentation, open your web browser and navigate to: http://localhost:8081/swagger-ui/#/recipe-management-controller
![img.png](src/main/resources/img.png)
The Swagger UI provides a user-friendly interface to interact with the API, test endpoints, and view request and response details.

