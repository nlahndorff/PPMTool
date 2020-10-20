package org.plaidcat.PPMTool;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, SecondaryValidation.class, TertiaryValidation.class})
public interface ValidationSequence {

}
