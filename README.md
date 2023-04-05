# AdminUtils
Plugin project, this plugins helps you track how many blocks
# In block break you write the name of the block you want to be tracked, 
  and the value will be the number of times it's broken before it notifies the admins.
  
blockbreak:
  DIAMOND_ORE: 10
# In the message you can write your own custom message, make sure to follow the patter below. This message is displayed when a player breaks the limit of blocks   before notification to the admins, this messasge when clicked will tp the admin to the player.
message: %player% ha minado %number% bloques de %type%
# The color of the message above.
color: RED
