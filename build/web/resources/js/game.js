/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var connection, actualColor = 0, color, peacesInBoard, peacesInWaiting;
let welcomeSection = document.querySelector('#welcomeGame');
let gameSection = document.querySelector('#boardGame');

//inicia o jogo e abre nova conexão
document.querySelector('#playGame').addEventListener('click', function(event){
    event.preventDefault();
    
    connection = new WebSocketConnection();
    connection.openConnection();
    
    welcomeSection.style.display = 'none';
    gameSection.style.display = 'block';
});

function coordinates(cell) {
    var dc = cell.cellIndex;
    var dr = cell.parentNode.rowIndex;
    return [dr, dc];
}

/*function drag(ev) {
    ev.dataTransfer.effectAllowed = 'move';
    ev.dataTransfer.setData("text/plain", "[" + coordinates(this.parentNode) + "]");
}*/
function allowDrop(ev) {
    ev.preventDefault();
}

//monta o boardgame e atualiza
function montarTabela(boardgame) {
    let table = document.getElementsByTagName("table")[0];
    boardgame.forEach(function (row, rowIndex) {
        row.forEach(function (col, colIndex) {
            var cell = table.rows[rowIndex].cells[colIndex];
            cell.style.backaground = (col === 0 ? "" : (col === 3 ? 'blue' 
                    : 'red'));
                    
            /*var x = cell.firstChild;
            if (x) {
                x.draggable = true;
                x.ondragstart = function(ev){
                    ev.dataTransfer.effectAllowed = 'move';
                    ev.dataTransfer.setData("text/plain", "[" + coordinates(this.parentNode) + "]");
                };
            }
            cell.ondragover = function(ev){
                ev.preventDefault();
            };
            cell.ondrop = function(ev) {
                ev.preventDefault();
                connection.webSocket.send(JSON.stringify({origem: JSON.parse(ev.dataTransfer.getData("text/plain")), destino: coordinates(this), type:status}));
            };*/
            
            /*cell.onmousemove = function(ev){
                //ev.preventDefault();
                //cell[0].style.backgroundColor = "yellow";  
            };*/

            //inserir peça no tabuleiro
            cell.onclick = function(event){
                event.preventDefault();
                console.log("entrou");
                connection.webSocket.send(JSON.stringify({origem:[], location: coordinates(this)}));
            };
        });
    });
}
