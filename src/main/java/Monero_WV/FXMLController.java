/* 
 * Copyright (C) 2018 R.Harkes & M.van Gorcum
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Monero_WV;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


public class FXMLController implements Initializable {
    @FXML
    private TextField wallet_txt;
    @FXML
    private AnchorPane APane;
    @FXML
    private Label msg;
    
    
    @FXML
    private void handleupdate_wallet(ActionEvent event) {
        Wallet wallet = new Wallet(wallet_txt.getText());
        Color c = wallet.valid ? Color.GREEN : Color.RED;
        String error = wallet.valid ? "wallet OK": wallet.GetError();
        msg.setBackground(new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
        msg.setText(error);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
