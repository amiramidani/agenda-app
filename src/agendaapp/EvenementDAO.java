package agendaapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {

    public void ajouter(Evenement e) throws SQLException {
        String sql = "INSERT INTO evenement (titre, date_evenement, heure, description, type_evenement, important) VALUES (?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?)";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, e.getTitre());
        ps.setString(2, e.getDateEvenement());
        ps.setString(3, e.getHeure());
        ps.setString(4, e.getDescription());
        ps.setString(5, e.getTypeEvenement());
        ps.setInt(6, e.isImportant() ? 1 : 0);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public List<Evenement> afficherTous() throws SQLException {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT id, titre, TO_CHAR(date_evenement, 'YYYY-MM-DD') AS date_evenement, heure, description, type_evenement, important FROM evenement ORDER BY date_evenement";
        Connection conn = ConnexionDB.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));
            e.setTitre(rs.getString("titre"));
            e.setDateEvenement(rs.getString("date_evenement"));
            e.setHeure(rs.getString("heure"));
            e.setDescription(rs.getString("description"));
            e.setTypeEvenement(rs.getString("type_evenement"));
            e.setImportant(rs.getInt("important") == 1);
            list.add(e);
        }
        rs.close();
        st.close();
        conn.close();
        return list;
    }

    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM evenement WHERE id = ?";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public void modifier(Evenement e) throws SQLException {
        String sql = "UPDATE evenement SET titre=?, date_evenement=TO_DATE(?, 'YYYY-MM-DD'), heure=?, description=?, type_evenement=?, important=? WHERE id=?";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, e.getTitre());
        ps.setString(2, e.getDateEvenement());
        ps.setString(3, e.getHeure());
        ps.setString(4, e.getDescription());
        ps.setString(5, e.getTypeEvenement());
        ps.setInt(6, e.isImportant() ? 1 : 0);
        ps.setInt(7, e.getId());
        ps.executeUpdate();
        ps.close();
        conn.close();
    }

    public List<Evenement> rechercherParTitre(String titre) throws SQLException {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT id, titre, TO_CHAR(date_evenement, 'YYYY-MM-DD') AS date_evenement, heure, description, type_evenement, important FROM evenement WHERE LOWER(titre) LIKE ?";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + titre.toLowerCase() + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));
            e.setTitre(rs.getString("titre"));
            e.setDateEvenement(rs.getString("date_evenement"));
            e.setHeure(rs.getString("heure"));
            e.setDescription(rs.getString("description"));
            e.setTypeEvenement(rs.getString("type_evenement"));
            e.setImportant(rs.getInt("important") == 1);
            list.add(e);
        }
        rs.close();
        ps.close();
        conn.close();
        return list;
    }

    public List<Evenement> rechercherParDate(String date) throws SQLException {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT id, titre, TO_CHAR(date_evenement, 'YYYY-MM-DD') AS date_evenement, heure, description, type_evenement, important FROM evenement WHERE date_evenement = TO_DATE(?, 'YYYY-MM-DD')";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, date);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));
            e.setTitre(rs.getString("titre"));
            e.setDateEvenement(rs.getString("date_evenement"));
            e.setHeure(rs.getString("heure"));
            e.setDescription(rs.getString("description"));
            e.setTypeEvenement(rs.getString("type_evenement"));
            e.setImportant(rs.getInt("important") == 1);
            list.add(e);
        }
        rs.close();
        ps.close();
        conn.close();
        return list;
    }

    public List<Evenement> rechercherParType(String type) throws SQLException {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT id, titre, TO_CHAR(date_evenement, 'YYYY-MM-DD') AS date_evenement, heure, description, type_evenement, important FROM evenement WHERE LOWER(type_evenement) LIKE ?";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + type.toLowerCase() + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));
            e.setTitre(rs.getString("titre"));
            e.setDateEvenement(rs.getString("date_evenement"));
            e.setHeure(rs.getString("heure"));
            e.setDescription(rs.getString("description"));
            e.setTypeEvenement(rs.getString("type_evenement"));
            e.setImportant(rs.getInt("important") == 1);
            list.add(e);
        }
        rs.close();
        ps.close();
        conn.close();
        return list;
    }

    public List<Evenement> afficherImportants() throws SQLException {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT id, titre, TO_CHAR(date_evenement, 'YYYY-MM-DD') AS date_evenement, heure, description, type_evenement, important FROM evenement WHERE important = 1 ORDER BY date_evenement";
        Connection conn = ConnexionDB.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Evenement e = new Evenement();
            e.setId(rs.getInt("id"));
            e.setTitre(rs.getString("titre"));
            e.setDateEvenement(rs.getString("date_evenement"));
            e.setHeure(rs.getString("heure"));
            e.setDescription(rs.getString("description"));
            e.setTypeEvenement(rs.getString("type_evenement"));
            e.setImportant(rs.getInt("important") == 1);
            list.add(e);
        }
        rs.close();
        st.close();
        conn.close();
        return list;
    }

    public boolean existeDoublon(String titre, String date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenement WHERE LOWER(titre) = ? AND date_evenement = TO_DATE(?, 'YYYY-MM-DD')";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, titre.toLowerCase());
        ps.setString(2, date);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        ps.close();
        conn.close();
        return count > 0;
    }

    public boolean existeConflit(String date, String heure) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenement WHERE date_evenement = TO_DATE(?, 'YYYY-MM-DD') AND heure = ?";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, date);
        ps.setString(2, heure);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        ps.close();
        conn.close();
        return count > 0;
    }

    public boolean existeConflitModification(String date, String heure, int idExclu) throws SQLException {
        String sql = "SELECT COUNT(*) FROM evenement WHERE date_evenement = TO_DATE(?, 'YYYY-MM-DD') AND heure = ? AND id != ?";
        Connection conn = ConnexionDB.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, date);
        ps.setString(2, heure);
        ps.setInt(3, idExclu);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        ps.close();
        conn.close();
        return count > 0;
    }

    public void exporterFichier(String nomFichier, List<Evenement> liste) throws java.io.IOException {
        java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(nomFichier));
        pw.println("========================================");
        pw.println("       AGENDA PERSONNEL - EXPORT");
        pw.println("========================================");
        pw.println();
        for (Evenement e : liste) {
            pw.println("ID          : " + e.getId());
            pw.println("Titre       : " + e.getTitre());
            pw.println("Date        : " + e.getDateEvenement());
            pw.println("Heure       : " + e.getHeure());
            pw.println("Description : " + e.getDescription());
            pw.println("Type        : " + e.getTypeEvenement());
            pw.println("Important   : " + (e.isImportant() ? "Oui" : "Non"));
            pw.println("----------------------------------------");
        }
        pw.println("Nombre total d'événements : " + liste.size());
        pw.close();
    }
}