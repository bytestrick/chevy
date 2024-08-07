- [Assets store](https://itch.io/gameView-assets)
- [Linear interpolation](https://en.wikipedia.org/wiki/Linear_interpolation)

# Cose

- I nemici:
  - [x] Wraith
  - [x] Zombie
  - [x] Slime
  - [x] Big slime
  - [x] Beetle
  - [x] Skeleton
- e le trappole:
  - [x] Spiked floor
  - [x] Icy floor
  - [x] Sludge
- [x] Aggiungere le animazioni al Player (ho messo alcuni archivi con delle immagini di player) (Attenzione la durata delle animazioni deve corrispondere alla durata dello stato a meno che non si imposti `loop = true` nell'`AnimatedSprite` per info guardare la classe `BeetleView`)
- [ ] le impostazioni per gestire l'audio e i parametri per il resize
- [ ] la classe per gestire l'audio
- [ ] la gestione dei livelli: passaggio da un livello all'altro (quando si prendono le scale)
- [ ] salvataggio delle informazioni dei livelli e delle impostazioni (livello bloccato/completato)

- Problemi relativi all'attacco
    - Il giocatore può solo sferrare attacchi quando è adiacente al nemico.
    - Sferrare gli attacchi tramite i tasti usati per il movimento è inusuale.
    - La direzione del giocatore non si aggiorna in base alla direzione
      dell'attacco. Esempio: se il giocatore è rivolto verso l'alto è attacca
      a sinistra, l'attacco sembra venire rivolto verso l'alto.
    - Questi tre punti si possono risolvere introducendo dei tasti appositi per
      l'attacco: `ijkl`. in modo tale che sia supportato anche l'attacco a
      distanza e il cambio di direzione.

Sono stati tutti implementati sia la parti relativa a model e sia la parte relativa alla view.

La classe `GameSettings` contiene informazioni utili sulle impostazioni di gioco.

La classe `WindowSettings` contiene informazioni utili sul far diventare la finestra e il gioco responsive.

Le classi nel package `chamber` gestiscono il caricamento delle stanze nel gioco, mentre, `chamber/drawOrder` gestisce l'ordine con cui le entità devono essere ridisegnate

Le classi dentro al package `pathFinding` gestiscono la logica per trovare il cammino di costo minimo (in modo euristico)

Le classi dentro al package `stateMachine` implementano la macchina a stati finiti

Le classi dentro il package `entity` sono tutte funzionanti (a patto di bug) tranne:
- `entity/projectile/Arrow` (l'implementazione dovrebbe essere uguale a quella fatta per `entity/projectTile/SlimeShot` da cambiare solo le imaggini che compongono le animazioni per il relativa classe della view. I frame per l'Arrow sono in `chevy/res/asset/projectile/arrow`), tale proiettile viene usato dalla trappola Totem
- `entity/StaticEntity/Environment/Trap/`
  - `Totem`
  - `TrapDoor`
  - `Void` (dovrebbe funzionare ma non l'ho testata)
- `entity/StaticEntity/Environment/`
  - `Barrier`
  - `Chest`
- `entity/StaticEntity/PowerUp`
  - nessun `powerUp` é stato implementato

Possibili cose da evitare: non implementare i power up e le chest.

### Entities
- Static entities:
  - PowerUp:
    - Thunderbolt: Fulmini colpiscono periodicamente tutti i nemici. 
    - Holy Shield: Riduce i danni in arrivo dopo essere stati colpiti.
    - Vampire Fangs: chance di recuperare 1HP durante un attacco.
    - Angel Ring: il giocatore viene rianimato quando muore per cause diverse dalla caduta.
    - Swift Boots: Incrementa la velocità di movimento.
    - Long Sword: Incrementa il danno da spada di 5.
    - God's Ice: Conegela periodicamente i nemici.
    - Ignition: Gli attacchi hanno la possibilità di incendiare i nemici.
    - Coin of Greed: I nemici rilasciano più monete.
    - Hot Heart: Incrementa gli HP al massimo.
    - Cold Heart: Incrementa lo scudo di 3.
    - Gorgon's Poison: Elimini i nemici con un colpo, ma gli HP decrementano ad 1 permanentemente.
    - Stone Boots: Le trappole chiodate non hanno effetto.
    - Broken Arrows: Le freccie non ti infliggono danno.
    - Agility: Possibilità di evitare un attacco.
    - Hedgehog Spines: Riflette una parte di danno al tuo aguzzino.
    - Cat Claw: Aumenta la velocità d'attacco.
    - Slime Piece: Il danno d'attacco aumenta di 8, ma dopo la morte i nemici generano piccoli slime.
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

### Attribuzione

- Il ninja è una versione modificata di [Superpowers Asset Packs](https://github.com/sparklinlabs/superpowers-asset-packs) (CC0)
