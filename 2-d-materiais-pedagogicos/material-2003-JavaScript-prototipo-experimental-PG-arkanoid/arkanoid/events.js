///////////////////////////////////////////////////////////////////////////////////////////////////
// FUNCIONALIDEDES PÚBLICAS. O QUE NECESSÁRIO CONHECER PARA UTILIZAR ESTE MÓDULO. /////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Objecto event:
 *
 *  Propriedades:
 *		// Propriedades da interface Event
 *		type - tipo do evento (nome do evento sem 'on' - ex. evento onclick type="click").
 *		target - referência para o objecto que representao elemento sobre o qual 
 *				 foi gerado o evento.
 *
 *		// Propriedades da interface MouseEvent
 *		clientX - coordenada X,  da zona útil do browser (zona onde é mostrado o conteúdo da página HTML), onde o rato foi clicado.
 *		clientY - coordenada Y,  da zona útil do browser (zona onde é mostrado o conteúdo da página HTML), onde o rato foi clicado.
 *		ctrlKey – booleano que indica o estado da tecla de CTRL.
 *		altKey – booleano que indica o estado da tecla de ALT.
 *		shiftKey – booleano que indica o estado da tecla de SHIFT.
 *		button – valor numérico que indica o botão do rato clicado. 
 *			0 – botão esquerdo.
 *			1 – botão central.
 *			2 – botão direito.
 *		// Propriedades não standard, mas suportadas pelos dois browsers.
 *		keyCode – código da tecla premida (em Unicode). 
 *
 *  Métodos:
 *		stopPropagation – para a propagação do evento para o elemento acima na hierarquia. 
 *		preventDefault–  anula o comportamento por omissão do evento (anula o evento)
 */



/**
 * getEvent
 * retorna um objecto Event com algumas das propriedades definidas na especificação DOM.
 *
 * @returns objecto Event.
 */

function getEvent() {
	var evt;
	if(window.navigator.appName != "Netscape") {
		// Internet Explorer
		evt = createExplorerEvent();
	} else {
		evt = createNetscapeEvent();
	}
	return evt;
}


///////////////////////////////////////////////////////////////////////////////////////////////////
// FUNÇÕES INTERNAS. NÂO É NECESSÁRIO O SEU CONHECIMENTO PÚBLICO. /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


var IE2DOM_BUTTONS = [0,0,2,-1,1];

function createExplorerEvent() {
	var evt = new Object();
	
	////////////////
	// Attributes //
	////////////////
	/**
	 * Attributes from Event interface
	 */
	evt.type = event.type;
	evt.target = event.srcElement;
	
	/**
	 * Attributes from MouseEvent interface
	 */
	evt.screenX = event.screenX;
	evt.screenY = event.screenY;
	evt.clientX = event.clientX;
	evt.clientY = event.clientY;
	evt.ctrlKey = event.ctrlKey;
	evt.shiftKey = event.shiftKey;
	evt.altKey = event.altKey;
	evt.button = IE2DOM_BUTTONS[event.button];
	
	
	/**
	 * Not standard attributes but supported by both browsers
	 */
	 evt.keyCode = event.keyCode;

	
	/////////////
	// Methods //
	/////////////
	evt.stopPropagation = stopPropagation;
	evt.preventDefault = preventDefault;
	return evt;
}

function stopPropagation() {
	window.event.cancelBubble = true;
}

function preventDefault() {
	window.event.returnValue = false;
}


function createNetscapeEvent() {
	// Look up for a caller function with parameters, and with one of them that has
	// stopPropagation and preventDefault properties (I think two are sufficient)
	
	var f = getEvent.caller;
	
	for(;f ; f = f.caller) {
		if(f.arguments.length != 0) {
			var evt = f.arguments[0];
			if(Event.prototype.isPrototypeOf(evt)) {
				return evt;
			}
		} 
	}
	return null;
}
