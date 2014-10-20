(function () {
    window.InnerHTMLVerifier = (function () {
        return {
            saveState: function (representation, real_element) {
                representation.innerHTML = real_element.innerHTML;
            },
            checkChanges: function (representation, real_element) {
                if (representation.innerHTML !== real_element.innerHTML) {
                    var virtual_DOM = document.createElement(real_element.tagName),
                        virtual_childs = virtual_DOM.childNodes,
                        real_childs = real_element.childNodes,
                        mutation = null;
                    virtual_DOM.innerHTML = representation.innerHTML;
                    if (real_childs.length !== virtual_childs.length) {
                        for (var i = 0; i < real_childs.length; i++) {
                            if ( ! virtual_childs[i] ||
                                real_childs[i].textContent !== virtual_childs[i].textContent) {
                                if (mutation === null || mutation.target.innerHTML.length < real_childs[i].innerHTML.length)
                                    mutation = { target: real_childs[i], type: "innerHTML" };
                            }
                        };


                    }
                }
                if (mutation)
                    return mutation;
                return null;
            }
        };
    }());
}());
