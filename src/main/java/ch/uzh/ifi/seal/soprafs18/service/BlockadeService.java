package ch.uzh.ifi.seal.soprafs18.service;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.Blockade;
import ch.uzh.ifi.seal.soprafs18.repository.BlockadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlockadeService {

    private final BlockadeRepository blockadeRepository;

    @Autowired
    public BlockadeService(BlockadeRepository blockadeRepository) {this.blockadeRepository = blockadeRepository; }

    public Blockade generateBlockade (int id){
        Blockade blockade = new Blockade();
        switch(id) {

            case 1:
                blockade.setPower(1);
                blockade.setColor(PowerType.GREEN);
                break;
            case 2:
                blockade.setPower(1);
                blockade.setColor(PowerType.YELLOW);
                break;
            case 3:
                blockade.setPower(1);
                blockade.setColor(PowerType.DISCARD);
                break;
            case 4:
                blockade.setPower(1);
                blockade.setColor(PowerType.BLUE);
                break;
            case 5:
                blockade.setPower(2);
                blockade.setColor(PowerType.GREEN);
                break;
            case 6:
                blockade.setPower(2);
                blockade.setColor(PowerType.DISCARD);
                break;
            default:
                return null;

        }
        blockadeRepository.save(blockade);
        return blockade;
    }

}