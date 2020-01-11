/*
 * @author Guillermo Castaneda Echegaray
 * @version 0.6
 * @course COP 2805-23360
 * @instructor Jeho Park
 *
 * Program with basic CRUD functionality: Creates (inserts), Reads (views),
 * Updates or Deletes staff information stored in a database:
 * The Read (view) button displays the record for the specified ID.
 * The Create (insert) button inserts a new record with the specified ID.
 * The Update button updates the record for the specified ID.
 * The Delete button deletes the record for the specified ID.
 * The Clear button clears the form fields.
 */
package com.gce;

import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.System.exit;

public class SimpleJavaCrud extends JApplet {

    private static String databaseName = "sample_database";

    // Database credentials
    private static String databaseUser;
    private static String databasePass;

    // Database connection session
    private static Connection connection;

    // Frame panels
    private JPanel topPanel = new JPanel(new GridLayout(0, 2));
    private JPanel bottomPanel = new JPanel(new GridLayout(1, 4, 5, 5));

    // topPanel Form text fields
    private JTextField fID = new JTextField(2);
    private JTextField fLastName = new JTextField(15);
    private JTextField fFirstName = new JTextField(15);
    private JTextField fMI = new JTextField(1);
    private JTextField fAddress = new JTextField(20);
    private JTextField fCity = new JTextField(20);
    private JTextField fState = new JTextField(2);
    private JTextField fTelephone = new JTextField(10);
    private JTextField fEmail = new JTextField(40);
    private JTextField dbStatus = new JTextField(80);

    // bottomPanel Form buttons
    private JButton create = new JButton("Create");
    private JButton read = new JButton("Read");
    private JButton update = new JButton("Update");
    private JButton delete = new JButton("Delete");
    private JButton clear = new JButton("Reset");

    // For CRUD operations
    private String sql;
    private String[] rowData;

    /**
     * Main method
     *
     * @param args Passed arguments
     */
    public static void main(String[] args) {

        // Instantiates the applet
        SimpleJavaCrud applet = new SimpleJavaCrud();

        // Initialize and Start the applet
        applet.init();
        applet.start();

        // Create the frame container
        JFrame frame = new JFrame();

        // Set frame parameters
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Castaneda - SimpleJavaCrud");
        frame.getContentPane().add(applet, BorderLayout.CENTER);
        frame.setSize(390, 340);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                closeApplet();
            }
        });
    }

    @Override
    public void init() {
        // Authenticates the user's MySQL and creates the database if it doesn't already exist
        authenticateMysql();

        // Sets the border layout
        setLayout(new BorderLayout());

        // Draws the form panels and adds the form elements
        drawTopPanel();
        drawButtonPanel();
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        clearForm("Successfully connected.");

        // Listens for button clicks
        actionListeners();
    }

    /**
     * Draws the field labels and input areas for each field
     */
    private void drawTopPanel() {
        // Add the form elements to the top panel
        topPanel.setBorder(new TitledBorder("Staff information"));
        topPanel.add(dbStatus);
        topPanel.add(new JLabel(""));
        dbStatus.setEnabled(false);
        dbStatus.setEditable(false);
        topPanel.add(new JLabel("ID (9)"));
        topPanel.add(fID);
        topPanel.add(new JLabel("Last Name (15)"));
        topPanel.add(fLastName);
        topPanel.add(new JLabel("First Name (15)"));
        topPanel.add(fFirstName);
        topPanel.add(new JLabel("MI (1)"));
        topPanel.add(fMI);
        topPanel.add(new JLabel("Address (20)"));
        topPanel.add(fAddress);
        topPanel.add(new JLabel("City (20)"));
        topPanel.add(fCity);
        topPanel.add(new JLabel("State (2)"));
        topPanel.add(fState);
        topPanel.add(new JLabel("Telephone (10)"));
        topPanel.add(fTelephone);
        topPanel.add(new JLabel("E-mail (40)"));
        topPanel.add(fEmail);
    }

    /**
     * Draws the buttons on the button panel
     */
    private void drawButtonPanel() {
        // Create a pretty border around the top panel
        bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Add the buttons to the bottom panel
        bottomPanel.add(create); // Button 1 (Creates a record)
        bottomPanel.add(read);   // Button 2 (Reads a record)
        bottomPanel.add(update); // Button 3 (Updates a record)
        bottomPanel.add(delete); // Button 4 (Deletes a record)
        bottomPanel.add(clear);  // Button 5 (Clears the form)
    }

    /**
     * Listen for user clicks on CRUD buttons
     */
    private void actionListeners() {
        // Listen to the Create button
        create.addActionListener((ActionEvent c) -> {

            sql = "insert into `staff` "
                    + "(`id`, `lastname`, `firstname`, `mi`, `address`, `city`, `state`, `telephone`, `email`) "
                    + "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

            rowData = new String[]{
                    fID.getText(),
                    fLastName.getText(),
                    fFirstName.getText(),
                    fMI.getText(),
                    fAddress.getText(),
                    fCity.getText(),
                    fState.getText(),
                    fTelephone.getText(),
                    fEmail.getText(),
            };

            // Execute CRUD operation
            crud(sql, rowData, "create");
        });

        // Listen to the Read button
        read.addActionListener((ActionEvent r) -> {

            sql = "select * from `staff` where id = ?;";

            rowData = new String[]{fID.getText()};

            // Execute CRUD operation
            crud(sql, rowData, "read");
        });

        // Listen to the Update button
        update.addActionListener((ActionEvent u) -> {

            sql = "update `staff` "
                    + "set `lastname` = ?, "
                    + "`firstname` = ?, "
                    + "`mi` = ?, "
                    + "`address` = ?, "
                    + "`city` = ?, "
                    + "`state` = ?, "
                    + "`telephone` = ?, "
                    + "`email` = ? "
                    + "where id = ?";

            rowData = new String[]{
                    fLastName.getText(),
                    fFirstName.getText(),
                    fMI.getText(),
                    fAddress.getText(),
                    fCity.getText(),
                    fState.getText(),
                    fTelephone.getText(),
                    fEmail.getText(),
                    fID.getText(),
            };

            // Execute CRUD operation
            crud(sql, rowData, "update");
        });

        // Listen to the Delete button
        delete.addActionListener((ActionEvent d) -> {
            sql = "delete from `staff` where id = ?";

            rowData = new String[]{
                    fID.getText()
            };

            // Execute CRUD operation
            crud(sql, rowData, "delete");
        });

        // Listen to the clear form button
        clear.addActionListener((ActionEvent l) -> clearForm("Ready!"));
    }

    /**
     * Clears the form and displays a message
     *
     * @param message The feedback message
     */
    private void clearForm(String message) {
        dbStatus.setText(message);
        fID.setText("");
        fLastName.setText("");
        fFirstName.setText("");
        fMI.setText("");
        fAddress.setText("");
        fCity.setText("");
        fState.setText("");
        fTelephone.setText("");
        fEmail.setText("");
    }

    /**
     * Connects to the database
     * Assumptions:
     * 1. The MySQL Java Connector is properly included in the project's libraries
     * 2. Will connect to host:localhost at default MySQL port 3306. TODO: Ask user for host & port
     * 3. Does not use SSL connection to the database
     *
     * @param databaseUser the mysql database username
     * @param databasePass the mysql database password
     * @param databaseName The name of the created database
     */
    private void dbConnect(String databaseUser, String databasePass, String databaseName) {
        try {
            // Database connection driver, URL, and schema name
            String mysqlDriver = "com.mysql.jdbc.Driver";
            Class.forName(mysqlDriver);
            // Disabled the use of SSL to prevent display of the SSL warning on the console
            String connectionURL = "jdbc:mysql://localhost:3306/";
            connection = DriverManager.getConnection(
                    connectionURL + databaseName + "?useSSL=false", databaseUser, databasePass);
        } catch (ClassNotFoundException ex) {
            JOptionPane.showConfirmDialog(
                    null, "MySQL Driver not found.\nPlease install and run the program " +
                            "again.\nClick OK to exit applet.",
                    "Connect to MySQL - Missing Driver", JOptionPane.OK_OPTION);
            // Without the mysqlDriver installed, the applet cannot continue
            exit(0);
        } catch (SQLException ex) {
            // Invalid credentials. Typos can happen. Will call the authentication
            // method again or exit the applet if cancelled.
            int inputUser = JOptionPane.showConfirmDialog(
                    null, "Invalid MySQL Credentials.\nPlease check your " +
                            "username/password and try again.",
                    "Connect to MySQL - Connection Error", JOptionPane.OK_CANCEL_OPTION);

            // Exit the applet if the cancel button is pressed
            if (inputUser == JOptionPane.CANCEL_OPTION) {
                closeApplet();
            }

            // Pressing OK will prompt the user again for authentication credentials
            authenticateMysql();
        }
    }

    /**
     * Executes CRUD operations.
     *
     * @param sql     The sql query
     * @param rowData array of row values to bind parameters
     * @param action  create, read, update, or delete the requested record
     */
    private void crud(String sql, String[] rowData, String action) {
        dbConnect(databaseUser, databasePass, databaseName);

        try {
            // Prepares the sql query statement
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Binds the query statement parameters
            for (int i = 0; i < rowData.length; i++) {
                preparedStatement.setString(i + 1, rowData[i]);
            }

            // Because the Read/View action does not modify the database
            // only the SELECT query is executed
            if (action.equals("read")) {
                // Execute the query statement
                ResultSet resultSet = preparedStatement.executeQuery();

                // The result set is not empty, populate the form fields
                if (resultSet.next()) {
                    fLastName.setText(resultSet.getString(2));
                    fFirstName.setText(resultSet.getString(3));
                    fMI.setText(resultSet.getString(4));
                    fAddress.setText(resultSet.getString(5));
                    fCity.setText(resultSet.getString(6));
                    fState.setText(resultSet.getString(7));
                    fTelephone.setText(resultSet.getString(8));
                    fEmail.setText(resultSet.getString(9));
                    dbStatus.setText("Showing Record #" + fID.getText() + ".");
                } else {
                    // The result set is empty, clear the form and display the message
                    clearForm("Record #" + fID.getText() + " does not exist.");
                }

                // Closes the result set
                resultSet.close();
            } else {
                if (fID.getText() != null && !fID.getText().isEmpty()) {
                    // Executes the Create/Update/Delete operation
                    if (preparedStatement.executeUpdate() != 0) {
                        // The crud action was successful
                        clearForm("Record #" + fID.getText() + " " + action + "d.");
                    } else {
                        // The crud action was unsuccessful
                        clearForm("Error. Record not " + action + "d.");
                    }
                } else {
                    clearForm("You must enter a Record ID.");
                }

                // Closes the preparedStatement
                preparedStatement.close();
            }
        } catch (SQLException e2) {
            // System.out.println(e2);
        } finally {
            // Closes the database connection
            try {
                connection.close();
            } catch (SQLException e) {
                // System.out.println(e);
            }
        }
    }

    /**
     * Authenticates the MySQL user
     */
    private void authenticateMysql() {
        // Prompt for the MySQL username
        JTextField username = new JTextField(20);

        int inputUser = JOptionPane.showConfirmDialog(
                null,
                username,
                "Connect to MySQL - Username",
                JOptionPane.OK_CANCEL_OPTION
        );

        databaseUser = username.getText();

        // Exit the applet if the cancel button is pressed
        if (inputUser == JOptionPane.CANCEL_OPTION) {
            exit(0);
        }

        // Prompt for the MySQL password
        // For security reasons, the password input is masked
        JPasswordField password = new JPasswordField(20);

        int inputPass = JOptionPane.showConfirmDialog(
                null,
                password,
                "Connect to MySQL - Password",
                JOptionPane.OK_CANCEL_OPTION
        );

        databasePass = new String(password.getPassword());

        // Exit the applet if the cancel button is pressed
        if (inputPass == JOptionPane.CANCEL_OPTION) {
            exit(0);
        }

        try {
            // Create the database if it doesn't already exist
            createDatabase(databaseUser, databasePass);

        } catch (SQLException e) {
            // e.printStackTrace(); // debug
        }
    }

    /**
     * Creates the database schema and table if they do not already exist.
     *
     * @param databaseUser The local MySQL user name
     * @param databasePass The local MySQL user password
     */
    private void createDatabase(String databaseUser, String databasePass) throws SQLException {
        try {
            // We are creating the database schema so we pass an empty value for databaseName
            dbConnect(databaseUser, databasePass, "");

            // No user input is used to create the schema or the database, so there is no need
            // to use prepared statements.
            Statement statement = connection.createStatement();

            String schemaSql = "CREATE SCHEMA IF NOT EXISTS " + databaseName + " DEFAULT CHARACTER SET utf8";

            String tableSql = "CREATE TABLE IF NOT EXISTS " + databaseName + ".staff (" +
                    "id CHAR(9) NOT NULL, " +
                    "lastname VARCHAR(15) NULL, " +
                    "firstname VARCHAR(15) NULL, " +
                    "mi CHAR(1) NULL, " +
                    "address VARCHAR(20) NULL, " +
                    "city VARCHAR(20) NULL, " +
                    "state CHAR(2) NULL, " +
                    "telephone CHAR(10) NULL, " +
                    "email VARCHAR(40) NULL, " +
                    "PRIMARY KEY (id), " +
                    "UNIQUE INDEX id_unique (id ASC))";

            // Create the schema
            statement.executeUpdate(schemaSql);

            // Create the table
            statement.executeUpdate(tableSql);

            // Close the statement
            statement.close();
        } catch (SQLException se) {
            // System.out.println(se);
            // se.printStackTrace();
        } finally {
            try {
                // Close the database connection
                connection.close();
            } catch (SQLException e) {
                // System.out.println(e); // debug
            }
        }
    }

    /**
     * Displays a goodbye dialog when closing the applet
     */
    public static void closeApplet() {
        JOptionPane.showMessageDialog(
                null,
                "Applet has terminated.\nGoodBye!");
        exit(0);
    }
}
