/**
 * @file switchify.js
 * @brief switchify jQuery extension.
 * @version 1.0.0 (2017-12-06)
 * @date 2017-12-06
 * @author Borja García Quiroga <@borjagq>
 * 
 * 
 * Copyright (c) 2016 Borja García Quiroga
 * 
 * This code is licensed under MIT license (see LICENSE for details).
 */

 (function ($) {

    /**
     * @brief Transforms the checkbox into a switch.
     * 
     * Transforms the checkbox into a switch.
     * 
     * @author Borja García Quiroga <@borjagq>
     */
    $.fn.switchify = function() {

        // Make _self an alias of the jQuery object.
        var _self = this;

		// Check if the object isn't already an input.
        // If it's not, we'll transform it into one.
        if (!_self.is('input')) {

            let attributes = {};

            $.each(_self[0].attributes, function(_, attr) {
                attributes[attr.nodeName] = attr.nodeValue;
            });

            // Replace the element with a newly created input that contains the
            // same attributes.
            let newSelf = $('<input />', attributes);
            _self.replaceWith(newSelf);
            _self = newSelf;

        }

        // If the object isn't already an input of type checkbox, we'll
        // transform it into one.
        _self.attr('type', 'checkbox');

        // Put the input element into the switchify label container.
        _self.wrap('<label class="switchify switchify-label"></label>');
        _self.addClass('switchified');
        var _selfLabel = _self.parent();

        // Add the desired objects.
        _selfLabel.append('<span class="switchify-switch"></span>');

        return _selfLabel;

    }

})(jQuery);
