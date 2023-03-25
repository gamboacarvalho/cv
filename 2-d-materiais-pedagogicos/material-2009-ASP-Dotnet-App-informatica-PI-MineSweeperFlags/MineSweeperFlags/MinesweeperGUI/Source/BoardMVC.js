function BoardMVC(lines, cols, cell, gKey) {

    var current = this; 


    // Board Model --------------------------------------------------------------------------------------

    this.boardModel = function() {

        if (this.boardModel.getLines != undefined) return;

        this.boardModel.getLines = function() { return lines; }
        this.boardModel.getCols = function() { return cols; }

    }


    // Board Controller ---------------------------------------------------------------------------------
    this.boardController = function() {
        if (this.boardController.getLines != undefined) return;
        this.boardController.getLines = function() { return current.boardModel.getLines(); }
        this.boardController.getCols = function() { return current.boardModel.getCols(); }

        this.boardController.start = function() {
            for (var i = 0; i < this.getLines(); i++) {
                for (var j = 0; j < this.getCols(); j++) {
                    var currCell = current.boardView.getCellByPos(j, i);
                    cell.addEventListener(currCell);
                }
            }
        }        
        
    }
    
    // Board View ---------------------------------------------------------------------------------------
    this.boardView = function() {
        if (this.boardView.render != undefined) return;

        this.boardView.render = function() {                
            var cellWidth = $("." + HIDDEN_CELL).css("width");
            var width = (cellWidth * current.boardController.getCols());

            for (var i = 0; i < current.boardController.getLines(); i++) {
                for (var j = 0; j < current.boardController.getCols(); j++) {                
                    cell.create(undefined, j, i).appendTo($("#" + getRealId( gKey,  BOARD_CLASS )));
                }
            }
        }

        this.boardView.getCellByPos = function(posX, posY) {
            if (posX >= current.boardController.getCols() || posY >= current.boardController.getLines() || posX < 0 || posY < 0)
                return null;
                
            return document.getElementById( cell.getId(posX, posY) );
        }

    }

    this.init = function() {
        current.boardModel();
        current.boardView();
        current.boardController();
        current.boardView.render();
    }
}