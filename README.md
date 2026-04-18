# 🕒 Timezone Servlet Application

A custom Java Servlet application built for Apache Tomcat to dynamic processing and rendering of the current time across multiple timezones. Featuring automatic routing, strict timezone validations, browser cookie persistence, and dynamically rendered UI via Thymeleaf.

## ✨ Features

- **Timezone Querying**: Pass a `timezone` (e.g. `UTC+2`, `Europe/Kyiv`) as a URL parameter to recalculate real-time output.
- **Server-side Templating**: Integration with `Thymeleaf` to handle sleek HTML templating gracefully. 
- **Persisted User Settings**: Seamlessly saves your last valid timezone preferences explicitly into `Cookies`, enabling intelligent parameter fallback on subsequent visits.
- **Strict Validations (`Filter`)**: Validates the timezone using `java.util.TimeZone`. Any incorrect request string immediately returns `HTTP 400 Invalid timezone` seamlessly bypassing the handler.
- **Jakarta/Java EE Standardized**: Built utilizing typical `javax.servlet` mapping parameters, explicitly optimized for deployment on `Tomcat 9`.

## 🛠 Prerequisites

Ensure your environment includes the following essential prerequisites installed:
- [Java Development Kit (JDK 11+)](https://adoptium.net/)
- [Apache Maven](https://maven.apache.org/) - For dependency aggregation and build flow setup.
- [Apache Tomcat 9](https://tomcat.apache.org/download-90.cgi)

## 🚀 Installation & Running

1. **Clone the repository:**
   ```bash
   git clone https://github.com/1neyzer1/timezone-servlet-app.git
   cd timezone-servlet-app
   ```

2. **Build the WAR package via Maven:**
   ```bash
   mvn clean package
   ```
   *This command aggregates all class configurations globally and builds the `.war` deployment bundle into the `/target` directory.*

3. **Deploy it on Tomcat Application Server:**
   - Copy the successfully generated `.war` file (e.g., `timezone-servlet-app-1.0-SNAPSHOT.war`) from the `target/` directory over to `/webapps` located inside your native Tomcat server's distribution architecture.
   - Run Tomcat server.
   
   *(Alternatively, run the configuration inherently through IntelliJ IDEA via your native Maven/Tomcat configuration integrations).*

## 📖 Usage Examples

Once properly deployed, visit your standard Localhost bindings (e.g. `http://localhost:8080/time`)!

- **Base Parameter:** 
  `http://localhost:8080/time` returns basic current UTC time (if no cookie is active).
  
- **Custom Timezone Passing:** 
  Visit `http://localhost:8080/time?timezone=UTC+2` yielding `2022-01-05 12:05:01 UTC+2` globally on the screen.

- **Check Incorrect Behaviors:** 
  Visit `http://localhost:8080/time?timezone=Invalid_Zone` which reliably restricts processing via `TimezoneValidateFilter`.

---
*Created as part of Java coursework assignments.*
