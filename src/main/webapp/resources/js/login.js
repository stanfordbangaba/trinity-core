/**
 * Handles login ajax callback
 */

function handleLoginRequest(xhr, status, args) {
	if(args.validationFailed || !args.loggedIn) {
		PF('dlg').jq.effect("shake", {times:5}, 100);
	}
	else {
		PF('dlg').hide();
	    $('#loginLink').fadeOut();
	    window.location.href = '/trinity-core/index.xhtml';
	}
}