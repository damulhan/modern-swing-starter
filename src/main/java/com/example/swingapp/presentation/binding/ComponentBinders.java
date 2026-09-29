package com.example.swingapp.presentation.binding;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;

import javax.swing.*;

/**
 * Utility helper to streamline creation of bound Swing components with FlatLaf styling.
 * Reduces repetitive boilerplate between View and PresentationModel.
 */
public final class ComponentBinders {

    private ComponentBinders() {}

    /**
     * Creates a JTextField bound to a ValueModel with a FlatLaf placeholder.
     */
    public static JTextField bindTextField(ValueModel valueModel, String placeholder) {
        JTextField textField = BasicComponentFactory.createTextField(valueModel);
        if (placeholder != null && !placeholder.isEmpty()) {
            textField.putClientProperty("JTextField.placeholderText", placeholder);
        }
        return textField;
    }

    /**
     * Creates a JCheckBox bound to a ValueModel.
     */
    public static JCheckBox bindCheckBox(ValueModel valueModel, String label) {
        return BasicComponentFactory.createCheckBox(valueModel, label);
    }
}
