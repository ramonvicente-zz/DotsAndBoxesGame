/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Arrays;

/**
 *
 * @author ramon
 */
public class BoardGame {
    private int turn = 0;
    public int bluePoints = 0;
    public int redPoints = 0;
    public static int BLUE = 3;
    public static int RED = 4;
    
    //linha que pode ser selecionável
    private int selectableLine = 1;
    
    //0 = pontos; 1 - linhas selecionaveis 2 - box em branco
    private int[][] boardgame = {
        {0, 1, 0, 1, 0, 1, 0, 1, 0},
        {1, 2, 1, 2, 1, 2, 1, 2, 1},
        {0, 1, 0, 1, 0, 1, 0, 1, 0},
        {1, 2, 1, 2, 1, 2, 1, 2, 1},
        {0, 1, 0, 1, 0, 1, 0, 1, 0},
        {1, 2, 1, 2, 1, 2, 1, 2, 1},
        {0, 1, 0, 1, 0, 1, 0, 1, 0},};
   

    public int getTurn() {
        return turn;
    }
    
    //verifica se á linhas para selecionar
    public boolean hasSelectableLine(){
        for(int i = 0; i < boardgame.length; i++){
            for(int j = 0; j < boardgame.length; j++){
                if(boardgame[i][j]==2){
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean move(int player, int positionOriginX, int positionOriginY, 
            int positionDestineX, int positionDestineY){
        
        /* É a sua vez de jogar? */
        if ((player == RED && turn == 0) || (player == BLUE && turn == 1)) {
            return false;
        }
       
        /* Origem e destino devem ser diferentes */
        if (positionOriginX == positionDestineX && positionOriginY == positionDestineY) {
            return false;
        }
        
        //Pode inserir neste campo?
        if(boardgame[positionDestineX][positionDestineY] == 3){
            return false;
        }
        
        /* Origem e destino estão no tabuleiro */
        if ((positionOriginX < 0 || positionOriginX >= 7) || (positionOriginY < 0 || positionOriginY >= 7) 
                || (positionDestineX < 0 || positionDestineX >= 7) || (positionDestineY < 0 || positionDestineY >= 7)) {
            return false;
        }
        /* Origem possui uma peça? */
        if (boardgame[positionOriginX][positionOriginY] == 0) {
            return false;
        }
        /* Destino deve estar vazio */
        if (boardgame[positionDestineX][positionDestineY] != 0) {
            return false;
        }
        boolean ok = false;
        
        /* Jogada normal*/
        if(boardgame[positionDestineX][positionDestineY] == 0){
            boardgame[positionOriginX][positionOriginY] = 0;
            boardgame[positionDestineX][positionDestineY] = player;
            ok = true;
        }
       
        if (ok) {
            turn = (turn + 1) % 2;
        }
        return ok;
        
    }
    
    public boolean selectLine(int player, int positionX, int positionY){
        
        /* É a sua vez de jogar? */
        if ((player == RED && turn == 0) || (player == BLUE && turn == 1)) {
            return false;
        }
        
        //Pode inserir neste campo?
        if(boardgame[positionX][positionY] == 0){
            return false;
        }
        
        /* Destino deve estar vazio */
        if (boardgame[positionX][positionY] != 1) {
            return false;
        }
        boolean ok = false;
        if(player == BLUE){
            boardgame[positionX][positionY] = BLUE;
            ok = true;
        }
        if(player == RED){
            boardgame[positionX][positionY] = RED;
            ok = true;
        }
        
        if (ok) {
            turn = (turn + 1) % 2;
        }
        return ok;
    }
    
    //fez um moinho ou trilha
    public boolean isMill(int player, int positionX, int positionY){/*
        if( ((boardgame[positionX][positionY-1]==player)&&(boardgame[positionX][positionY+1]==player))||
            ((boardgame[positionX][positionY+1]==player)&&(boardgame[positionX][positionY+2]==player))||
            ((boardgame[positionX][positionY-1]==player)&&(boardgame[positionX][positionY-2]==player))||
            ((boardgame[positionX-1][positionY]==player)&&(boardgame[positionX+1][positionY]==player))||
            ((boardgame[positionX+1][positionY]==player)&&(boardgame[positionX+2][positionY]==player))||
            ((boardgame[positionX-1][positionY]==player)&&(boardgame[positionX-2][positionY]==player))  ){
            //torna o turno de volta para o jogador
            turn = (turn + 1) % 2;
            return true;
        }
        
        ((boardgame[0][0]==player)&&(boardgame[0][3]==player) && boardgame[0][6]==player)||
            ((boardgame[0][0]==player)&&(boardgame[3][0]==player) && boardgame[6][0]==player)||
            ((boardgame[0][0]==player)&&(boardgame[0][3]==player) && boardgame[0][6]==player)||
            ((boardgame[0][0]==player)&&(boardgame[0][3]==player) && boardgame[0][6]==player)||)
        */
        int linePeacesCount = 0;
        int columnPeacesCount = 0;
        
        for(int line = 0; line < boardgame.length; line++){
            if(line == positionX){
                for(int column = 0; column < boardgame[0].length; column++){
                    if(boardgame[line][column]==player){   
                        linePeacesCount++;
                    }
                }
            }
        }
        for(int column = 0; column < boardgame[0].length; column++){
            if(column == positionY){
                for(int line = 0; line < boardgame[0].length; line++){
                    if(boardgame[line][column]==player){   
                        columnPeacesCount++;
                    }
                }
            }
        }
        if(linePeacesCount == 3 || columnPeacesCount == 3){
            turn = (turn + 1) % 2;
            return true;
        }
      
        return false;
    }
    
    public int playerPeaceQuantity(int player){
        return 1;
    }
    
    @Override
    public String toString() {
        return Arrays.deepToString(boardgame);
    }
}
