// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

private lateinit var openSetting: () -> Unit

@Composable
@Preview
fun App() {
    var subject by remember { mutableStateOf("") }
    var receivers by remember { mutableStateOf("") }
    var contents by remember { mutableStateOf("") }

    var showSetting by remember { mutableStateOf(false) }
    var sendResult by remember { mutableStateOf("") }

    openSetting = { showSetting = true }

    DesktopMaterialTheme {
        if (showSetting) {
            SettingWdiget {
                showSetting = false
            }
        } else {
            Column(modifier = Modifier.padding(all = 12.dp)) {
                Button(
                    enabled = subject.isNotBlank() && receivers.isNotBlank() && contents.isNotBlank(),
                    onClick = {
                        sendResult = "发送中..."
                        sendResult = SendTask.send(subject, receivers, contents)
                    }) {
                    Text("Send~")
                }
                Text(text = sendResult, color = Color.Red)
                OutlinedTextField(
                    value = subject,
                    label = { Text("标题") },
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = { subject = it }
                )
                OutlinedTextField(
                    value = receivers,
                    label = { Text("收件人") },
                    placeholder = { Text("批量复制黏贴收件人, 一行一个.") },
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
                    onValueChange = { receivers = it }
                )
                OutlinedTextField(
                    value = contents,
                    label = { Text("正文") },
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    onValueChange = { contents = it }
                )
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        icon = painterResource("app_icon.ico"),
        title = "WeiWeiMail"
    ) {
        MenuBar {
            Menu("设置", mnemonic = 'S') {
                Item("账户") {
                    AccountInfo.loadAccountInfo()
                    openSetting.invoke()
                }
                Separator()
                Item("关于", onClick = {

                })
            }
        }
        App()
    }
}
