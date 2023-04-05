# AdminUtils
Plugin project, this plugins helps you track how many blocks
### In blockbreak you have to specify the block to be tracked, has to be the java MATERIAl enum, and the value of the block is the number of times it's broken before triggering the message to the admins.

```yml
blockbreak:
  DIAMOND_ORE: 10
```

### In the message you can write your own custom message, make sure to follow the patter below. This message is displayed when a player breaks the limit of blocks before notification to the admins, this messasge when clicked will tp the admin to the player.

```yml
message: %player% ha minado %number% bloques de %type%
```

### The color of the message above.

```yml
color: RED
```
