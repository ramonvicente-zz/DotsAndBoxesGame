/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function WebSocketConnection() {
    this.webSocket = null;
    let _self = this;
    let webSocketUrl = "ws://" + document.location.host 
            + document.location.pathname + "dotsAndBox";
    
    this.writeResponse = function(text) {
        let messages = document.querySelector("#results");
        messages.innerHTML = text;
    };
    
    this.openConnection = function(){
        try{
            this.webSocket = new WebSocket(webSocketUrl);
            this.webSocket.binaryType = "arraybuffer";

            this.webSocket.onopen = function(){
                _self.writeResponse("Esperando outro jogador...");
            };

            this.webSocket.onmessage = function(messageEvent){
                let msg = JSON.parse(messageEvent.data);
                switch (msg.type) {
                    case 0:
                        /* Informando cor da peça do usuário atual */
                        cor = msg.color;
                        break;
                    case 1:
                        /* Recebendo o tabuleiro modificado */
                        corDaVez = msg.turn;
                        _self.writeResponse((corDaVez === cor) ? "É a sua vez de jogar." : "É a vez do adversário de jogar.");
                        montarTabela(msg.tabuleiro);
                        break;
                    case 3:
                        corDaVez = msg.turn;
                        _self.writeResponse((corDaVez === cor) ? "Você fez um moinho, remova uma peça do adversário." : 
                                "O adversário fez um moinho, uma peça sua será removida.");
                        montarTabela(msg.tabuleiro);
                        break;
                    case 2:
                        /* Fim do jogo */
                        _self.writeResponse(msg.message);
                        this.webSocket.close();
                        break;
                }
            };

            this.webSocket.onerror = function(errorEvent){
                _self.writeResponse("error: " + errorEvent.data);
            };

            this.webSocket.onclose = function(closeEvent){
                _self.writeResponse("Connection closed.");
            };

        } catch(exception){
            console.error(exception);
        }
    };
}