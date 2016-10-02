// Define a grammar
grammar TSM;

r
  : group_expression (COMMA group_expression)*
  | items
  ;

group_expression
  : group_structure
  | group_structure COMMA items
  ;

group_structure
  : GROUP_PREFIX group_name subgroup*
  ;

subgroup
  : GROUP_SEPARATOR group_name
  ;

GROUP_PREFIX
  : 'group:'
  ;

group_name
  : GROUP_CHARACTER+
  | ITEM_ID (GROUP_CHARACTER ITEM_ID)*
  | ITEM_ID GROUP_CHARACTER*
  | GROUP_CHARACTER+ (GROUP_CHARACTER ITEM_ID)*
  ;

GROUP_CHARACTER
  : CHARACTER
  | CHARACTER_ADDITIONAL
  ;

GROUP_SEPARATOR
  : '`'
  ;

items
  : item (COMMA item)*
  ;

item
  : ITEM_PREFIX item_id
  ;

ITEM_PREFIX
  : 'i:'
  ;

item_id
  : ITEM_ID
  ;

ITEM_ID
  : NUMBER+
  ;
  
NUMBER
  : [0-9]
  ;

CHARACTER
  : [a-zA-Z]
  ;

CHARACTER_ADDITIONAL
  : ' '
  | '_'
  | '-'
  | '+'
  | '/'
  ;

COMMA
  : ','
  ;

// skip spaces, tabs, newlines
WS
  : [\t\r\n]+ -> skip
  ;