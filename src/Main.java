import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class Main {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/CricketDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "system";

    // Player class
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

    // CricketTeam class
    static class CricketTeam {
        private ArrayList<Player> players;

        public CricketTeam() {
            players = new ArrayList<>();
        }

        // Add player to database
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

        // Display players
        public void displayPlayers(JTable table) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                    ArrayList<Object[]> data = new ArrayList<>();
                    while (rs.next()) {
                        int id = rs.getInt("player_id");
                        String name = rs.getString("name");
                        int age = rs.getInt("age");
                        String role = rs.getString("role");
                        double battingAvg = rs.getDouble("batting_average");
                        int jerseyNumber = rs.getInt("jersey_number");
                        data.add(new Object[] {id, name, age, role, battingAvg, jerseyNumber});
                    }

                    Object[][] tableData = new Object[data.size()][6];
                    for (int i = 0; i < data.size(); i++) {
                        tableData[i] = data.get(i);
                    }

                    String[] columnNames = {"ID", "Name", "Age", "Role", "Batting Average", "Jersey Number"};
                    table.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Search player by name
        public void findPlayerByName(String name, JTable table) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players WHERE name LIKE ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, "%" + name + "%");
                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Object[]> data = new ArrayList<>();
                        while (rs.next()) {
                            int id = rs.getInt("player_id");
                            String playerName = rs.getString("name");
                            int age = rs.getInt("age");
                            String role = rs.getString("role");
                            double battingAvg = rs.getDouble("batting_average");
                            int jerseyNumber = rs.getInt("jersey_number");
                            data.add(new Object[] {id, playerName, age, role, battingAvg, jerseyNumber});
                        }

                        Object[][] tableData = new Object[data.size()][6];
                        for (int i = 0; i < data.size(); i++) {
                            tableData[i] = data.get(i);
                        }

                        String[] columnNames = {"ID", "Name", "Age", "Role", "Batting Average", "Jersey Number"};
                        table.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Delete player from database
        public void deletePlayerFromDatabase(int playerId) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "DELETE FROM Players WHERE player_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, playerId);
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Player deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Player not found.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error deleting player.");
            }
        }

        // Display players by role
        public void displayPlayersByRole(String role, JTable table) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players WHERE role = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, role);
                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Object[]> data = new ArrayList<>();
                        while (rs.next()) {
                            int id = rs.getInt("player_id");
                            String playerName = rs.getString("name");
                            int age = rs.getInt("age");
                            double battingAvg = rs.getDouble("batting_average");
                            int jerseyNumber = rs.getInt("jersey_number");
                            data.add(new Object[] {id, playerName, age, role, battingAvg, jerseyNumber});
                        }

                        Object[][] tableData = new Object[data.size()][6];
                        for (int i = 0; i < data.size(); i++) {
                            tableData[i] = data.get(i);
                        }

                        String[] columnNames = {"ID", "Name", "Age", "Role", "Batting Average", "Jersey Number"};
                        table.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Display players with batting average above a certain value
        public void displayPlayersAboveBattingAvg(double avg, JTable table) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM Players WHERE batting_average > ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setDouble(1, avg);
                    try (ResultSet rs = stmt.executeQuery()) {
                        ArrayList<Object[]> data = new ArrayList<>();
                        while (rs.next()) {
                            int id = rs.getInt("player_id");
                            String playerName = rs.getString("name");
                            int age = rs.getInt("age");
                            double battingAvg = rs.getDouble("batting_average");
                            int jerseyNumber = rs.getInt("jersey_number");
                            data.add(new Object[] {id, playerName, age, battingAvg, jerseyNumber});
                        }

                        Object[][] tableData = new Object[data.size()][5];
                        for (int i = 0; i < data.size(); i++) {
                            tableData[i] = data.get(i);
                        }

                        String[] columnNames = {"ID", "Name", "Age", "Batting Average", "Jersey Number"};
                        table.setModel(new javax.swing.table.DefaultTableModel(tableData, columnNames));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Main GUI class
    public static void main(String[] args) {
        JFrame frame = new JFrame("Cricket Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        CricketTeam team = new CricketTeam();

        JPanel inputPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel displayPanel = new JPanel();

        JTextField nameField = new JTextField(15);
        JTextField ageField = new JTextField(5);
        JTextField roleField = new JTextField(10);
        JTextField avgField = new JTextField(5);
        JTextField jerseyField = new JTextField(5);

        String[] columnNames = {"ID", "Name", "Age", "Role", "Batting Average", "Jersey Number"};
        JTable playerTable = new JTable(new Object[0][columnNames.length], columnNames);
        JScrollPane tableScrollPane = new JScrollPane(playerTable);

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

        JButton addButton = new JButton("Add Player");
        JButton displayButton = new JButton("Display Players");
        JButton searchButton = new JButton("Search Player");
        JButton roleButton = new JButton("Display By Role");
        JButton avgButton = new JButton("Display By Avg");
        JButton deleteButton = new JButton("Delete Player");

        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(roleButton);
        buttonPanel.add(avgButton);
        buttonPanel.add(deleteButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(tableScrollPane, BorderLayout.SOUTH);

        // Add action listeners for buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    String role = roleField.getText();
                    double avg = Double.parseDouble(avgField.getText());
                    int jersey = Integer.parseInt(jerseyField.getText());

                    Player newPlayer = new Player(0, name, age, role, avg, jersey);
                    team.addPlayerToDatabase(newPlayer);
                    JOptionPane.showMessageDialog(frame, "Player added successfully.");
                    team.displayPlayers(playerTable); // Update table after adding player
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers for age, batting average, and jersey number.");
                }
            }
        });

        // Display all players
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                team.displayPlayers(playerTable);
            }
        });

        // Search player by name
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(frame, "Enter player name to search:");
                if (name != null && !name.trim().isEmpty()) {
                    team.findPlayerByName(name, playerTable);
                }
            }
        });

        // Display players by role
        roleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String role = JOptionPane.showInputDialog(frame, "Enter player role to filter by (e.g., Batsman, Bowler):");
                if (role != null && !role.trim().isEmpty()) {
                    team.displayPlayersByRole(role, playerTable);
                }
            }
        });

        // Display players with batting average above a certain value
        avgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double avg = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter minimum batting average:"));
                    team.displayPlayersAboveBattingAvg(avg, playerTable);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid number for the batting average.");
                }
            }
        });

        // Delete player
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String input = JOptionPane.showInputDialog(frame, "Enter Player ID to delete:");
                    if (input != null && !input.trim().isEmpty()) {
                        int playerId = Integer.parseInt(input);
                        team.deletePlayerFromDatabase(playerId);
                        team.displayPlayers(playerTable); // Refresh the table after deletion
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid numeric ID.");
                }
            }
        });

        frame.setVisible(true);
    }
}
