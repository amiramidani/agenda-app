package agendaapp;

public class Evenement {
    private int id;
    private String titre;
    private String dateEvenement;
    private String heure;
    private String description;
    private String typeEvenement;
    private boolean important;

    public Evenement() {}

    public Evenement(String titre, String dateEvenement, String heure, String description, String typeEvenement, boolean important) {
        this.titre = titre;
        this.dateEvenement = dateEvenement;
        this.heure = heure;
        this.description = description;
        this.typeEvenement = typeEvenement;
        this.important = important;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDateEvenement() { return dateEvenement; }
    public void setDateEvenement(String dateEvenement) { this.dateEvenement = dateEvenement; }

    public String getHeure() { return heure; }
    public void setHeure(String heure) { this.heure = heure; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTypeEvenement() { return typeEvenement; }
    public void setTypeEvenement(String typeEvenement) { this.typeEvenement = typeEvenement; }

    public boolean isImportant() { return important; }
    public void setImportant(boolean important) { this.important = important; }

    public String getImportantText() { return important ? "⭐ Oui" : "Non"; }
}