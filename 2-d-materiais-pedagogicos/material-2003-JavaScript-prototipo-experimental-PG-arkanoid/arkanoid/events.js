///////////////////////////////////////////////////////////////////////////////////////////////////
// FUNCIONALIDEDES P�BLICAS. O QUE NECESS�RIO CONHECER PARA UTILIZAR ESTE M�DULO. /////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Objecto event:
 *
 *  Propriedades:
 *		// Propriedades da interface Event
 *		type - tipo do evento (nome do evento sem 'on' - ex. evento onclick type="click").
 *		target - refer�ncia para o objecto que representao elemento sobre o qual 
 *				 foi gerado o evento.
 *
 *		// Propriedades da interface MouseEvent
 *		clientX - coordenada X,  da zona �til do browser (zona onde � mostrado o conte�do da p�gina HTML), onde o rato foi clicado.
 *		clientY - coordenada Y,  da zona �til do browser (zona onde � mostrado o conte�do da p�gina HTML), onde o rato foi clicado.
 *		ctrlKey � booleano que indica o estado da tecla de CTRL.
 *		altKey � booleano que indica o estado da tecla de ALT.
 *		shiftKey � booleano que indica o estado da tecla de SHIFT.
 *		button � valor num�rico que indica o bot�o do rato clicado. 
 *			0 � bot�o esquerdo.
 *			1 � bot�o central.
 *			2 � bot�o direito.
 *		// Propriedades n�o standard, mas suportadas pelos dois browsers.
 *		keyCode � c�digo da tecla premida (em Unicode). 
 *
 *  M�todos:
 *		stopPropagation � para a propaga��o do evento para o elemento acima na hierarquia. 
 *		preventDefault�  anula o comportamento por omiss�o do evento (anula o evento)
 */



/**
 * getEvent
 * retorna um objecto Event com algumas das propriedades definidas na especifica��o DOM.
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
// FUN��ES INTERNAS. N�O � NECESS�RIO O SEU CONHECIMENTO P�BLICO. /////////////////////////////////
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
