package boardgame.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Class representing the result of a game played by two players.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class GameResult {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The name of the first player.
     */
    @Column(nullable = false)
    private String player1;
    
    /**
     * The name of the second player.
     */
    @Column(nullable = false)
    private String player2;
    
    /**
     * The winner of the match.
     */
    @Column(nullable = false)
    private String winner;
    
    /**
     * Describes how the match ended.
     */
    @Column(nullable = false)
    private String winCondition;
    
    /**
     * The timestamp when the match was started.
     */
    @Column(nullable = false)
    private ZonedDateTime started;
    
    /**
     * The timestamp when the match ended.
     */
    @Column(nullable = false)
    private ZonedDateTime finished;

    /**
     * The duration of the game.
     */
    @Column(nullable = false)
    private Duration duration;

    /**
     * The timestamp when the result was saved.
     */
    @Column(nullable = false)
    private ZonedDateTime created;
    
    /**
     * Saves the creation timestamp before a match result is persisted.
     */
    @PrePersist
    protected void onPersist() {
        created = ZonedDateTime.now();
    }

}
