// Generated by esidl 0.4.0.

package org.w3c.dom.html;

import org.w3c.dom.NodeList;

public interface HTMLKeygenElement extends HTMLElement
{
    // HTMLKeygenElement
    public boolean getAutofocus();
    public void setAutofocus(boolean autofocus);
    public String getChallenge();
    public void setChallenge(String challenge);
    public boolean getDisabled();
    public void setDisabled(boolean disabled);
    public HTMLFormElement getForm();
    public String getKeytype();
    public void setKeytype(String keytype);
    public String getName();
    public void setName(String name);
    public String getType();
    public boolean getWillValidate();
    public ValidityState getValidity();
    public String getValidationMessage();
    public boolean checkValidity();
    public void setCustomValidity(String error);
    public NodeList getLabels();
}