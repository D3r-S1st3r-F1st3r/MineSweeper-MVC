package Model;

import View.BoardView;
import java.util.Random;

public class Model {

    FieldModel[][] fieldModel = new FieldModel[16][16];

    //random generator
    Random randomizer = new Random();

    //window size
    private int fieldWidth = 16;
    private int fieldHeight = 16;
    private int score = 0;


    //different counter
    private int flagCount;

    //Anzahl der Minen
    private int minenAnzahl;

    public void setMinenAnzahl(int anzahl){
        this.minenAnzahl = anzahl;
    }

    public void initFields(){
        for(int i = 0; i < fieldWidth; i++){
            for (int j = 0; j < fieldHeight; j++){
                fieldModel[i][j] = new FieldModel();
            }
        }
    }


    //setzt automatisch zufüllig Bomben im [][] Array
    public void setBombs() {
        for (int i = 0; i < minenAnzahl; i++) {

            int zahl1 = randomizer.nextInt(16);
            int zahl2 = randomizer.nextInt(16);

            if (!fieldModel[zahl1][zahl2].getBombActive()) {
                fieldModel[zahl1][zahl2].setBombActive();
                fieldModel[zahl1][zahl2].setBombValue(50);
            }else{
                i--;
            }
        }
    }

    public void initPoints() throws Exception {
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++){
                setValues(i,j);
            }
        }
    }

    //Werte werden gesetzt
    public void setValues(int i, int j) throws Exception {

        for (int xAchse = i - 1; xAchse <= i + 1; xAchse++) {
            for (int yAchse = j - 1; yAchse <= j + 1; yAchse++) {
                try {if (fieldModel[xAchse][yAchse].getBombActive()) {
                    fieldModel[i][j].setValue();
                }
                }
                catch (ArrayIndexOutOfBoundsException ao) {
                    continue;
                }
            }
        }
    }



    public boolean checkBomb(int i, int j){
        if(fieldModel[i][j].getBombActive() == true){
            return true;
        }else{
            setDisabledButton(i,j);
            return false;
        }
    }

    //Aktion, wenn Button gedrückt wurde
    public void setDisabledButton(int i, int j){
            fieldModel[i][j].setFlag(false);
            fieldModel[i][j].setShownActive();
    }

    //Aktion wenn Flagge gesetzt/entfernt wird
    public void setFlag(int i, int j){
        if(fieldModel[i][j].getFlag() == true){
            fieldModel[i][j].setFlag(false);
        }else{
            fieldModel[i][j].setFlag(true);
        }
    }

    public boolean getFlagStatus(int i, int j){
        return fieldModel[i][j].getFlag();
    }

    //Array mit allen Buttons werden gegebenen
    public FieldModel[][] getListWithPoints(){
        return this.fieldModel;
    }

    //Nachbarn werden geprüft. -> Wenn Zahl am Rand wird sie aufgedeckt, wenn 0 -> wird weitergesucht
    public void checkNeighborhood(int y, int x){

        //Wenn Feld = 0, wird if ausgeführt
        if(fieldModel[y][x].getValue() == 0){
            //Durch Nachbarfelder wird durch 2 For Schleifen iteriert
            for(int i = y-1; i <= y+1; i++){
                for(int j = x-1; j <= x+1; j++){

                    //durch try wird bei -1 in der x und y Achse ein Fehlerauswurf verhindert
                    try {
                        if (fieldModel[i][j].getShownStatus() == false) {

                            fieldModel[i][j].setShownActive();      //Punktescorer + setVisible Funktion

                            if(fieldModel[i][j].getValue() == 0){
                                checkNeighborhood(i,j);             //Wenn das aufgedeckte Feld eine 0 ist, wird der Vorgang von neu gestartet
                            }
                        }
                    }catch (ArrayIndexOutOfBoundsException ao){

                    }
                }
            }
        }
        else if(fieldModel[y][x].getValue() != 0 &&fieldModel[y][x].getShownStatus()){

        }
    }

    public int getMinesLeft(){
        return this.minenAnzahl;
    }

    public int checkPoints(){
        int output = 0;

        for(int i = 0; i < fieldModel.length;i++){
            for(int j = 0; j <fieldModel[i].length;j++){

                if(fieldModel[i][j].getShownStatus() == true){
                    output = output + fieldModel[i][j].getValue();
                }

            }
        }
        return output;

    }
}
