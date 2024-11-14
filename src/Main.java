import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Main {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/CricketDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "system";


    // Player class: Represents a player with name, age, role, batting average, and jersey number
    static class Player {
        private int id;
        private String name;
        private int age;
        private String role;
        private double battingAverage;
        private int jerseyNumber;

        public Player(int id, String name, int age, String role, double battingAverage, int jerseyNumber) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.role = role;
            this.battingAverage = battingAverage;
            this.jerseyNumber = jerseyNumber;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getRole() {
            return role;
        }

        public double getBattingAverage() {
            return battingAverage;
        }

        public int getJerseyNumber() {
            return jerseyNumber;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Age: " + age + ", Role: " + role + ", Batting Average: " + battingAverage + ", Jersey Number: " + jerseyNumber;
        }
    }

    // CricketTeam class: Manages the list of players and performs operations like add, display, and search
    static class CricketTeam {
        private ArrayList<Player> players;

        public CricketTeam() {
            players = new ArrayList<>();
        }

        // Insert a new player into the database
        public void addPlayerToDatabase(Player player) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "INSERT INTO Players (name, age, role, batting_average, jersey_number) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, player.getName());
                    stmt.setInt(2, player.getAge());
                    stmt.setString(3, player.getRole());
                    stmt.setDouble(4, player.getBattingAverage());
                    stmt.setInt(5, player.getJerseyNumber());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Display all players from the database
        public void displayPlayers(JTextArea displayArea) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    displayArea.append("List of Players:\n");
                    while (rs.next()) {
                        int id = rs.getInt("player_id");
                        String name = rs.getString("name");
                        int age = rs.getInt("age");
                        String role = rs.getString("role");
                        double battingAvg = rs.getDouble("batting_average");
                        int jerseyNumber = rs.getInt("jersey_number");
                        displayArea.append("ID: " + id + ", Name: " + name + ", Age: " + age + ", Role: " + role + ", Batting Average: " + battingAvg + ", Jersey Number: " + jerseyNumber + "\n");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Search for a player by name in the database
        public void findPlayerByName(String name, JTextArea displayArea) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players WHERE name LIKE ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, "%" + name + "%");
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int id = rs.getInt("player_id");
                            String playerName = rs.getString("name");
                            int age = rs.getInt("age");
                            String role = rs.getString("role");
                            double battingAvg = rs.getDouble("batting_average");
                            int jerseyNumber = rs.getInt("jersey_number");
                            displayArea.append("Player found: " + "ID: " + id + ", Name: " + playerName + ", Age: " + age + ", Role: " + role + ", Batting Average: " + battingAvg + ", Jersey Number: " + jerseyNumber + "\n");
                        } else {
                            displayArea.append("Player not found.\n");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Advanced Query: Display players with a specific role
        public void displayPlayersByRole(String role, JTextArea displayArea) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players WHERE role = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, role);
                    try (ResultSet rs = stmt.executeQuery()) {
                        displayArea.append("Players with Role " + role + ":\n");
                        while (rs.next()) {
                            int id = rs.getInt("player_id");
                            String playerName = rs.getString("name");
                            int age = rs.getInt("age");
                            double battingAvg = rs.getDouble("batting_average");
                            int jerseyNumber = rs.getInt("jersey_number");
                            displayArea.append("ID: " + id + ", Name: " + playerName + ", Age: " + age + ", Batting Average: " + battingAvg + ", Jersey Number: " + jerseyNumber + "\n");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Advanced Query: Display players above a certain batting average
        public void displayPlayersAboveBattingAvg(double avg, JTextArea displayArea) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players WHERE batting_average > ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setDouble(1, avg);
                    try (ResultSet rs = stmt.executeQuery()) {
                        displayArea.append("Players with Batting Average above " + avg + ":\n");
                        while (rs.next()) {
                            int id = rs.getInt("player_id");
                            String playerName = rs.getString("name");
                            int age = rs.getInt("age");
                            double battingAvg = rs.getDouble("batting_average");
                            int jerseyNumber = rs.getInt("jersey_number");
                            displayArea.append("ID: " + id + ", Name: " + playerName + ", Age: " + age + ", Batting Average: " + battingAvg + ", Jersey Number: " + jerseyNumber + "\n");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Main GUI class to handle user interaction and UI
    public static void main(String[] args) {
        // Create main frame
        JFrame frame = new JFrame("Cricket Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create an instance of CricketTeam to manage players
        CricketTeam team = new CricketTeam();

        // Create panels for layout
        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel displayPanel = new JPanel();

        // Create text fields for input
        JTextField nameField = new JTextField(15);
        JTextField ageField = new JTextField(5);
        JTextField roleField = new JTextField(10);
        JTextField avgField = new JTextField(5);
        JTextField jerseyField = new JTextField(5);
        JTextArea displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Set up input panel with labels and text fields
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Player Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(roleField);
        inputPanel.add(new JLabel("Batting Average:"));
        inputPanel.add(avgField);
        inputPanel.add(new JLabel("Jersey Number:"));
        inputPanel.add(jerseyField);

        // Set up button panel with buttons for different actions
        JButton addButton = new JButton("Add Player");
        JButton displayButton = new JButton("Display Players");
        JButton searchButton = new JButton("Search Player");
        JButton roleButton = new JButton("Display By Role");
        JButton avgButton = new JButton("Display By Avg");

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(roleButton);
        buttonPanel.add(avgButton);

        // Add panels to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Action listener for "Add Player" button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    String role = roleField.getText();
                    double battingAvg = Double.parseDouble(avgField.getText());
                    int jerseyNumber = Integer.parseInt(jerseyField.getText());

                    // Create new player and add to team and database
                    Player player = new Player(0, name, age, role, battingAvg, jerseyNumber);
                    team.addPlayerToDatabase(player);
                    displayArea.append("Player added: " + player + "\n");

                    // Clear input fields
                    nameField.setText("");
                    ageField.setText("");
                    roleField.setText("");
                    avgField.setText("");
                    jerseyField.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for Age, Batting Average, and Jersey Number.");
                }
            }
        });

        // Action listener for "Display Players" button
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayArea.setText(""); // Clear display area
                team.displayPlayers(displayArea);
            }
        });

        // Action listener for "Search Player" button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchName = JOptionPane.showInputDialog(frame, "Enter player name to search:");
                if (searchName != null && !searchName.trim().isEmpty()) {
                    team.findPlayerByName(searchName, displayArea);
                }
            }
        });

        // Action listener for "Display By Role" button
        roleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String role = JOptionPane.showInputDialog(frame, "Enter player role to search:");
                if (role != null && !role.trim().isEmpty()) {
                    team.displayPlayersByRole(role, displayArea);
                }
            }
        });

        // Action listener for "Display By Avg" button
        avgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double avg = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter minimum batting average:"));
                    team.displayPlayersAboveBattingAvg(avg, displayArea);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for batting average.");
                }
            }
        });

        // Set frame visible
        frame.setVisible(true);
    }
}
