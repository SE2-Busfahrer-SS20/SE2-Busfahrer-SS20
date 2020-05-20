package shared.networking.kryonet;


import java.util.ArrayList;

import shared.model.impl.PlayerImpl;
import shared.networking.dto.*;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.CheatedMessage;
import shared.networking.dto.ConfirmRegisterMessage;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.NewPlayerMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.ServerActionResponse;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.TextMessage;
import shared.networking.dto.PlayedMessage;
import shared.networking.dto.UpdateMessage;


public class NetworkConstants {

    public static final int TCP_PORT = 54555;
    // public static final int UDP_PORT = 54777;
    public static final String host ="192.168.0.115";

    // List for registered dto classes. Add needed classes to the array.
    public static final Class[] CLASS_LIST = {
            BaseMessage.class,
            TextMessage.class,
            RegisterMessage.class,
            CreateGameMessage.class,
            ServerActionResponse.class,
            ConfirmRegisterMessage.class,
            NewPlayerMessage.class,
            StartGameMessage.class,
            PlayedMessage.class,
            UpdateMessage.class,
            CheatedMessage.class,
            shared.model.impl.CardImpl[].class,
            shared.model.impl.CardImpl.class,
            java.util.ArrayList.class,
            PlayerImpl.class,
            com.esotericsoftware.kryonet.Connection.class,
            PlayedMessage.class,
            UpdateMessage.class,
            CheatedMessage.class,
            StartPLabMessage.class,
            DealPointsMessage.class,
            WinnerLooserMessage.class,
            ArrayList.class
    };

}
