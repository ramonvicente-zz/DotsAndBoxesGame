/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import models.BoardGame;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ramon
 */
@ServerEndpoint("/dotsAndBox")
public class GameEndpoint {
    
    private static Session player1, player2;
    private static BoardGame boardGame;
    
    @OnOpen
    public void onOpen(Session session) throws EncodeException, IOException{
        if (player1 == null) {
            player1 = session;
            player1.getBasicRemote().sendText("{\"type\": 0, \"color\": 0}");
            System.out.println(player1.getId() + " has opened a connection");
        } else if (player2 == null) {
            boardGame = new BoardGame();
            player2 = session;
            player2.getBasicRemote().sendText("{\"type\": 0, \"color\": 1}");
            System.out.println(player2.getId() + " has opened a connection");
            sendMessage("{\"type\": 1, \"tabuleiro\": " + boardGame + ", \"turn\": 0}");
        } else {
            session.close();
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws JSONException, IOException {
        JSONObject obj = new JSONObject(message);
        JSONArray origem = obj.getJSONArray("origem");
        JSONArray destino = obj.getJSONArray("destino");
        String tipo = obj.getString("type");
        
        if((boardGame.bluePeacesInWaiting > 0 || boardGame.redPeacesInWaiting > 0) && tipo.equals("regular")){;
            boardGame.selectLine(session == player1 ? BoardGame.BLUE : BoardGame.RED, destino.getInt(0), destino.getInt(1));    
            if (boardGame.isMill(session == player1 ? BoardGame.BLUE : BoardGame.RED, destino.getInt(0), destino.getInt(1))) {
                sendMessage("{\"type\": 2, \"tabuleiro\":"+boardGame+" ,\"message\": \"Um moinho foi feito.\", \"turn\": " + boardGame.getTurn() +"}");
            } else{
                sendMessage("{\"type\": 1, \"tabuleiro\": " + boardGame + ", \"turn\": " + boardGame.getTurn() + "}");
            }
            
        }else if(((boardGame.bluePeacesInWaiting == 0 && boardGame.bluePeacesInBoard > 2) || (boardGame.redPeacesInWaiting == 0 && boardGame.redPeacesInBoard > 2)) && tipo.equals("regular")){
            boardGame.move(session == player1 ? BoardGame.BLUE : BoardGame.RED, origem.getInt(0), origem.getInt(1), destino.getInt(0), destino.getInt(1));
            if (boardGame.isMill(session == player1 ? BoardGame.BLUE : BoardGame.RED, destino.getInt(0), destino.getInt(1))) {
                sendMessage("{\"type\": 2, \"tabuleiro\":"+boardGame+" ,\"message\": \"Um moinho foi feito.\", \"turn\": " + boardGame.getTurn() +"}");
            } else{
                sendMessage("{\"type\": 1, \"tabuleiro\": " + boardGame + ", \"turn\": " + boardGame.getTurn() + "}");
            }
            
        } 
        else if (boardGame.bluePeacesInBoard <= 2 && boardGame.bluePeacesInWaiting == 0) {
            sendMessage("{\"type\": 3, \"message\": \"Fim de Jogo. O jogador com peças vermelhas ganhou.\"}");
            player1.close();
            player2.close();
        }
        else if (boardGame.redPeacesInBoard <= 2 && boardGame.bluePeacesInWaiting == 0) {
            sendMessage("{\"type\": 3, \"message\": \"Fim de Jogo. O jogador com peças azuis ganhou.\"}");
            player1.close();
            player2.close();
        }
    }
    
    private void sendMessage(String msg) throws IOException {
        player1.getBasicRemote().sendText(msg);
        player2.getBasicRemote().sendText(msg);
        System.out.println(msg);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
    }
    
}
