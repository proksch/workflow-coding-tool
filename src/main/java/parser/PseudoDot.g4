/*
 * Copyright 2019 Sebastian Proksch
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

grammar PseudoDot;
@header {
   package parser;
}

graphs : digraph+ EOF;

digraph : 'strict' WS+ 'digraph' WS+ PART_ID WS* '{' edge+ WS* '}' WS*;

edge: WS* LABEL (WS* '->' WS* LABEL)* WS* ';' ;

LABEL: '"' ('\\"'|.)*? '"' ;

PART_ID : 'P' [1-9] [0-9]* ; 

WS: ' ' | '\t' | '\n';
