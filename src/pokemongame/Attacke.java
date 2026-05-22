package pokemongame;

/**
 * Repraesentiert eine Attacke, die von einem Pokemon erlernt werden kann.
 */
public class Attacke {

    private String name;
    private String typ;          
    private double staerke;      
    private String beschreibung;  
    private String effekt;       
    private double effektChance; 
    private double genauigkeit;
    private double atkMod;  
    private double defMod;
    private boolean targetIsSelf; 

    /**
     * Konstruktor 1: Erstellt eine normale Schadens- oder Hybrid-Attacke.
     */
    public Attacke(String name, String typ, double staerke, String effekt, double effektChance, double genauigkeit) {
        this.name = name;
        this.typ = typ;
        this.staerke = staerke;
        this.beschreibung = "";
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.genauigkeit = genauigkeit;
        this.atkMod = 1.0;
        this.defMod = 1.0;
        this.targetIsSelf = false;
    }

    /**
     * Konstruktor 2: Erstellt eine reine Status-Attacke ohne Direktschaden.
     */
    public Attacke(String name, String typ, String beschreibung, String effekt, double effektChance, double atkMod, double defMod, boolean targetIsSelf, double genauigkeit) {
        this.name = name;
        this.typ = typ;
        this.beschreibung = beschreibung;
        this.effekt = effekt;
        this.effektChance = effektChance;
        this.atkMod = atkMod;
        this.defMod = defMod;
        this.targetIsSelf = targetIsSelf;
        this.genauigkeit = genauigkeit;
        this.staerke = 0.0;
    }

    public String getName() { return name; }
    public String getTyp() { return typ; }
    public double getStaerke() { return staerke; }
    public String getBeschreibung() { return beschreibung; }
    public String getEffekt() { return effekt; }
    public double getEffektChance() { return effektChance; }
    public double getGenauigkeit() { return genauigkeit; }
    public double getAtkMod() { return atkMod; }
    public double getDefMod() { return defMod; }
    public boolean isTargetIsSelf() { return targetIsSelf; }
}