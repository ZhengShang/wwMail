import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun SettingWdiget(clickBack: () -> Unit) {
    var accountName by remember { mutableStateOf(AccountInfo.mailName) }
    var accountPw by remember { mutableStateOf(AccountInfo.mailPw) }

    Column(modifier = Modifier.padding(all = 12.dp)) {
        Row(
            modifier = Modifier.padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    AccountInfo.saveAccountInfo(accountName, accountPw)
                    clickBack.invoke()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Icon(painter = painterResource("ic_back.png"), contentDescription = "Back")
            }
            Text(text = "设置", modifier = Modifier.padding(start = 24.dp))
        }
        OutlinedTextField(
            value = accountName,
            label = { Text("账号") },
            modifier = Modifier.defaultMinSize(minWidth = 600.dp),
            onValueChange = { accountName = it }
        )
        OutlinedTextField(
            value = accountPw,
            label = { Text("密码") },
            placeholder = { Text("从后台获取的授权码, (不要填正式密码)") },
            modifier = Modifier.defaultMinSize(minWidth = 600.dp),
            onValueChange = { accountPw = it }
        )
    }
}