# Distributed Online Auction System

This project implements a real-time online auction system using Jakarta EE 10, demonstrating key enterprise features for building scalable and reliable distributed applications. The system supports user authentication, role-based access control, auction creation, real-time bidding, and live updates to clients.

## Features

*   **User Management:**
    *   Secure User Registration (default 'USER' role).
    *   User Login and Logout with token-based session management.
    *   Role-Based Authorization: 'ADMIN' and 'USER' roles.
*   **Auction Management:**
    *   Admin-only auction creation with detailed information (title, description, images, start/end time, category, starting price, min increment).
    *   View all auctions (publicly accessible).
    *   Detailed view for individual auctions.
    *   Auction status indication ('ACTIVE', 'WAITING', 'ENDED').
*   **Bidding System:**
    *   Real-time bid submission for authenticated users on active auctions.
    *   Bid validation (minimum increment, auction status).
    *   Automatic bid increment.
*   **Real-time Updates & Notifications:**
    *   Live bid updates pushed to connected clients using WebSockets.
    *   Cross-page notifications for bid updates on any auction.
*   **Asynchronous Processing:**
    *   JMS (Java Message Service) for asynchronous bid processing and real-time update broadcasting.
*   **In-Memory Data Storage:**
    *   All application data (users, auctions, categories, bid history) is managed in-memory using thread-safe `ConcurrentHashMap` and `CopyOnWriteArrayList` for demonstration purposes.

## Technologies Used

*   **Backend:** Jakarta EE 10 (EJB 4.0, JMS 3.0, JAX-RS 3.1, WebSockets 2.1)
*   **Application Server:** Payara Server 6 Community Edition
*   **JMS Provider:** Payara's embedded OpenMQ
*   **Build Tool:** Apache Maven
*   **Frontend:** HTML5, CSS (Tailwind CSS via CDN), JavaScript (Vanilla JS)
*   **Development Environment:** JDK 11+ (Recommended: JDK 17)

## Project Structure

The project follows a standard Maven multi-module structure:
Use code with caution.
Markdown
auction-system/
├── pom.xml (Parent POM)
├── ejb-module/ (EJB JAR: Contains EJBs, Entities, MDBs, Interfaces)
│ ├── pom.xml
│ └── src/main/java/com/auction/...
├── web-module/ (WAR: Contains JAX-RS Resources, WebSockets Endpoint, Frontend HTML/JS/JSP)
│ ├── pom.xml
│ └── src/main/webapp/...
├── ear-module/ (EAR: Bundles EJB JAR and WAR for deployment)
│ └── pom.xml
└── README.md

## Getting Started

Follow these instructions to set up and run the project locally.

### Prerequisites

*   **Java Development Kit (JDK) 11 or higher:**
    *   Ensure `JAVA_HOME` is set and `java` is in your system's PATH.
    *   **Recommended:** JDK 17, as Payara 6 is optimized for it and it resolves common class version errors.
*   **Apache Maven:**
    *   Ensure Maven is installed and its `bin` directory is in your system's PATH.
*   **Payara Server 6 Community Edition:**
    *   Download and extract Payara Server 6 to a known location (e.g., `C:\payara6`).

### 1. Configure Payara Server

First, you need to set up the JMS resources in your Payara domain.

1.  **Start your Payara domain:**
    Open a terminal/command prompt and navigate to your Payara's `bin` directory (e.g., `C:\payara6\bin`).
    ```bash
    ./asadmin start-domain
    ```

2.  **Create JMS resources (Queue, Topic, Connection Factory):**
    You can use the provided `.bat` script for Windows, or run the `asadmin` commands manually.

    ** Manual `asadmin` commands**
    *   Navigate to your Payara's `bin` directory (`C:\payara6\bin`).
    *   Run the following commands:
        ```bash
        ./asadmin create-jms-resource --restype jakarta.jms.Queue --property Name=BidQueue jms/BidQueue
        ./asadmin create-jms-resource --restype jakarta.jms.Topic --property Name=AuctionUpdatesTopic jms/AuctionUpdatesTopic
        ./asadmin create-jms-resource --restype jakarta.jms.ConnectionFactory --property AddressList=mq://localhost:7676 jms/ConnectionFactory
        ```
    *   You can verify resources with `./asadmin list-jms-resources`. Your application will use `jms/__defaultConnectionFactory` if `jms/ConnectionFactory` doesn't explicitly resolve or is not configured.

### 2. Build the Project

1.  **Navigate to the project root:**
    Open a terminal/command prompt and go to the `auction-system` directory (where this `README.md` and the parent `pom.xml` are located).
2.  **Clean and install the project:**
    ```bash
    mvn clean install
    ```
    This command compiles all modules, runs tests, and packages the application into `ear-module-1.0-SNAPSHOT.ear` (located in `ear-module/target/`).

### 3. Deploy the Application to Payara

1.  **Ensure Payara Domain is running:** If not, `cd C:\payara6\bin` and `./asadmin start-domain`.
2.  **Perform a Nuclear Clean (Recommended for fresh deployments):**
    This step ensures no old deployment artifacts interfere.
    *   Stop Payara domain: `cd C:\payara6\bin` and `./asadmin stop-domain`
    *   Manually delete the contents of these directories in your Payara domain (e.g., `C:\payara6\glassfish\domains\domain1\`):
        *   `applications`
        *   `generated`
        *   `osgi-cache`
    *   Start Payara domain again: `cd C:\payara6\bin` and `./asadmin start-domain`
3.  **Deploy the EAR file:**
    Open a terminal/command prompt and navigate to your Payara's `bin` directory (`C:\payara6\bin`).
    ```bash
    ./asadmin deploy C:\Users\User\IdeaProjects\auction-system\ear-module\target\ear-module-1.0-SNAPSHOT.ear
    ```
    (Adjust the path to your `ear-module` if it's different).

## Demo Users

The system is pre-populated with a few demo users:

*   **Regular User:** `user1` / `pass1` (Role: USER)
*   **Regular User:** `user2` / `pass2` (Role: USER)
*   **Administrator:** `admin1` / `pass1` (Role: ADMIN)

You can also register new users through the `login-register.html` page, which will be assigned the 'USER' role.

## Accessing the Application

Once successfully deployed, open your web browser and navigate to the main landing page:

`http://localhost:8080/auction-system/`

### Important UI Pages:

*   **Home Page (Marketing):** `http://localhost:8080/auction-system/` (or `index.jsp`)
*   **All Auctions (Public List):** `http://localhost:8080/auction-system/auctions.html`
*   **Login / Register:** `http://localhost:8080/auction-system/login-register.html`
*   **Create Auction (Admin Only):** `http://localhost:8080/auction-system/create-auction.html`
*   **Single Auction Details:** `http://localhost:8080/auction-system/auction-details.html?id=XXX` (replace `XXX` with an auction ID like `101`, `103`, etc.)

## Known Limitations

*   **In-Memory Data Persistence:** All application data (auctions, users, bids, categories) is stored exclusively in-memory. Data is lost upon server restart or application undeployment. For a production system, a persistent database (e.g., PostgreSQL with JPA) would be required.
*   **Horizontal Data Scalability:** The in-memory data store limits true horizontal scalability of data. In a clustered Payara environment, each node would have its own independent copy of the data, leading to inconsistencies without an external distributed cache.
*   **Password Hashing:** For demonstration, user passwords are stored and compared in plain text in memory. This is highly insecure and must be replaced with robust hashing (e.g., BCrypt) in a production environment.

## Contributing

Feel free to fork this repository, contribute, or provide feedback.

## License

This project is open-source and available under the [MIT License](LICENSE) (or other license you choose).
