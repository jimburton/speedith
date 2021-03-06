/*
 *   Project: Speedith.Core
 * 
 * File name: BasicInferenceRule.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2011 Matej Urbas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package speedith.core.reasoning;

import java.util.ArrayList;
import speedith.core.i18n.Translations;
import speedith.core.lang.CompoundSpiderDiagram;
import speedith.core.lang.Operator;
import speedith.core.reasoning.args.RuleArg;

/**
 * Represents a first-principle inference rule. These rules are the most
 * granular, work on primary diagrams and are verified in theory.
 * <p>A proof that consists of only basic inference rules is said to be
 * <span style="font-weight:bold;font-style:italic">trusted</span>.</p>
 * @param <TRuleArg> the type of the argument this rule takes.
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public interface BasicInferenceRule <TRuleArg extends RuleArg> extends InferenceRule<TRuleArg> {
}
