try {
    if (window.getAllAngularTestabilities) {
        // Run Angular Test readiness
        var testabilities = window.getAllAngularTestabilities();
        for (var i = 0; i < testabilities.length; i++) {
            if (!testabilities[i].isStable()) {
                return 'Angular testabilities are still running';
            }
        }
        return null;
    } else {
        // Run readiness on sites that don't use Angular
        try {
            function attemptSuccess() {
                if (!window['afReceived']) {
                    return 'AnimationFrame was not received';
                }
                window['afRequested'] = false;
                return null;
            }
            if (!window['afRequested']) {
                window['afRequested'] = true;
                window['afReceived'] = false;
                requestAnimationFrame(function () { window['afReceived'] = true});
            }
            if (document.readyState !== 'complete') {
                return 'Document is not ready'; // Page not loaded yet
            }
            if (window.jQuery) {
                if (window.jQuery.active) {
                    // jQuery is doing something
                    return 'jQuery is doing something';
                } else if (window.jQuery.ajax && window.jQuery.ajax.active) {
                    // We have an AJAX request processing.
                    return 'jQuery AJAX request in progress.';
                }
                if (window.jQuery("ap-root").length === 0) {
                    // We don't have any angular, so we don't have to really wait for anything else
                    return attemptSuccess();
                }
            } else {
                // No jQuery, we're done.
                return attemptSuccess();
            }
        } catch (ex) {
            console.error(ex);
            window.qa = window.qa || {};
            window.qa.error = ex;
            return 'Uknown error: ' + ex;
        }
    }
    throw new Error("End of 'busy.js' script reached, abnormal execution!");
} catch (ex) {
    console.error(ex);
    window.qa = window.qa || {};
    window.qa.error = ex;
    return 'Unknown error: ' + ex;
}