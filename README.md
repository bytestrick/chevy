- [Assets store](https://itch.io/gameView-assets)
- [Linear interpolation](https://en.wikipedia.org/wiki/Linear_interpolation)


### Entities
- Static entities:
  - PowerUp:
    - Thunderbolt: Fulmini colpiscono periodicamente tutti row nemici. 
    - Holy Shield: Riduce row danni in arrivo dopo essere stati colpiti.
    - Vampire Fangs: chance di recuperare 1HP durante un attacco.
    - Angel Ring: il giocatore viene rianimato quando muore per cause diverse dalla caduta.
    - Swift Boots: Incrementa la velocità di movimento.
    - Long Sword: Incrementa il danno da spada di 5.
    - God's Ice: Conegela periodicamente row nemici.
    - Ignition: Gli attacchi hanno la possibilità di incendiare row nemici.
    - Coin of Greed: I nemici rilasciano più monete.
    - Hot Heart: Incrementa gli HP al massimo.
    - Cold Heart: Incrementa lo scudo di 3.
    - Gorgon's Poison: Elimini row nemici con un colpo, ma gli HP decrementano ad 1 permanentemente.
    - Stone Boots: Le trappole chiodate non hanno effetto.
    - Broken Arrows: Le freccie non ti infliggono danno.
    - Agility: Possibilità di evitare un attacco.
    - Hedgehog Spines: Riflette una parte di danno al tuo aguzzino.
    - Cat Claw: Aumenta la velocità d'attacco.
    - Slime Piece: Il danno d'attacco aumenta di 8, ma dopo la morte row nemici generano piccoli slime.
    - Gold Arrow: Aumenta il danno delle frecce di 2.
    - Milk: +2 HP
    - Carrot: +1 HP
    - Piece of Bone: +5 scudi.
  - Environment:
    - Ground
    - Walls
    - Stairs
    - Barrier: circonda uno o più spazi vuoti per evitare di caderci dentro.
    - Chest: genera un powerUp randomico.
    - Trap:
      - Sludge: Blocca il giocatore di un movimento e poi scompare.
      - Void: Se il gioctore ci finisce dentro precipita.
      - Spiked floor: Dal pavimento spuntano periodicamente dei chiodi, infliggendo danno a chiunque si trovi sopra.
      - Trapdoor: Pezzo di legno sospeso nel vuoto, cade dopo un po' che ci stai sopra.
      - Totem: Spara periodicamente una freccia.
      - Icy Floor: Scivoli per tutta la durata del pavimento ghiacciato.
- Dynamic entities:
  - Environment:
    - Trap:
      - Circular saw blade: Una lama circolare che segue un percorso definito tagliando tutto ciò che incontra.
  - Player:
    - Knight: 10 HP, 2 SP, velocità: lenta, danno 5-7
    - Archer: 8 HP, 0 SP, velocità: media, danno 2-4
    - Ninja: 5 HP, 0 SP, velocità: alta, danno 3-5
  - Enemy:
    - Bat: si muove a caso. 2 HP, danno 2-3
    - Zombie: segue il player.
    - Wizard: si allinea nella riga del player per poi sparare una palla di fuoco che viaggio in linea retta.
    - Skeleton: segue il player, grazie al suo schudo resiste al primo attacco (non perde vita).
    - Big Slime: segue il player, alla morte genera due slime più piccoli nelle caselle confinanti, se disponibili.
    - Slime: si muovono nella direzione del player se il player si trova vicino ad un tot di blocchi.
    - Frog: Grazie alla sua lingua è in grado di colpire il player da un blocco di distanza.

