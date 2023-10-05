# Expense Tracker Application

This is a simple Expense Tracker application that helps you manage your expenses. The application allows you to read and track expenses from a Google Sheets document.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Google Sheets API Credentials](#google-sheets-api-credentials)
- [Running the Application](#running-the-application)
- [Endpoints](#endpoints)
- [API Documentation](#api-documentation)
- [Simplifying Expenses](#simplifying-expenses)
- [Monthly Statements](#monthly-statements)
- [Expense Calculator](#expense-calculator)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Java Development Kit (JDK) installed
- Spring Boot and Maven installed
- Google Sheets API credentials (JSON file)

## Getting Started
1. Clone this repository to your local machine.
2. Set up the Google Sheets API credentials (see [Google Sheets API Credentials](#google-sheets-api-credentials)).
3. Configure your Google Sheets document with your expense data.

## Google Sheets API Credentials
To connect to Google Sheets, you'll need API credentials. Follow these steps to set up your credentials:

1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing one.
3. Enable the "Google Sheets API" for your project.
4. Create credentials for a "Web application" and download the JSON file.
5. Place the JSON file in the `src/main/resources` directory of your project.

## Running the Application
To run the application, use the following command:

```bash
mvn spring-boot:run
