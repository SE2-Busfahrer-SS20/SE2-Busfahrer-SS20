package shared.networking.kryonet;

import java.util.ArrayList;
import java.util.List;

import shared.model.impl.PlayerDTOImpl;
import shared.model.impl.PlayerImpl;
import shared.networking.dto.*;

public class NetworkConstants {

    public static final int TCP_PORT = 54555;
    // public static final int UDP_PORT = 54777; //needs to stay commented, because on AAU server there is only one port available

    // List for registered dto classes. Add needed classes to the array.
    public static final Class<?>[] CLASS_LIST = {
            BaseMessage.class,
            TextMessage.class,
            RegisterMessage.class,
            CreateGameMessage.class,
            ServerActionResponse.class,
            ConfirmRegisterMessage.class,
            NewPlayerMessage.class,
            StartGameMessage.class,
            PlayedMessage.class,
            shared.model.GameState.class,
            UpdateMessage.class,
            shared.model.impl.CardImpl[].class,
            shared.model.impl.CardImpl.class,
            java.util.ArrayList.class,
            PlayerImpl.class,
            com.esotericsoftware.kryonet.Connection.class,
            UpdateMessage.class,
            CheatedMessage.class,
            PlayerDTOImpl.class,
            StartPLabMessage.class,
            DealPointsMessage.class,
            WinnerLooserMessage.class,
            ArrayList.class,
            BushmenMessage.class,
            BushmenCardMessage.class,
            CoughtMessage.class
    };

}
