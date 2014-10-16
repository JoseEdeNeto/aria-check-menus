(function () {
    window.InnerHTMLVerifier = (function () {
        return {
            saveState: function (representation, real_element) {
                representation.innerHTML = real_element.innerHTML;
            },
            checkChanges: function (representation, real_element) {
                if (real_element.innerHTML.indexOf(representation.innerHTML) === 0 &&
                    representation.innerHTML !== real_element.innerHTML) {
                    return { target: real_element, type: "innerHTML" };
                }
                return null;
            }
        };
    }());
}());
