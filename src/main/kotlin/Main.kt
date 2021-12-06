// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.io.File

private lateinit var openSetting: () -> Unit

@Composable
@Preview
fun App(window: ComposeWindow) {
    var subject by remember { mutableStateOf("") }
    var receivers by remember { mutableStateOf("") }
    var contents by remember { mutableStateOf("") }
    var attachmentPath by remember { mutableStateOf("") }
    var showSetting by remember { mutableStateOf(false) }
    var sendResult by remember { mutableStateOf("") }

    var attachmentFile: File? = null
    openSetting = { showSetting = true }

    DesktopMaterialTheme {
        if (showSetting) {
            SettingWdiget {
                showSetting = false
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(10.dp)
            ) {
                val stateVertical = rememberScrollState(0)
                Column(
                    modifier = Modifier.padding(all = 12.dp)
                        .verticalScroll(stateVertical)
                ) {
                    Button(
                        enabled = subject.isNotBlank() && receivers.isNotBlank() && contents.isNotBlank(),
                        onClick = {
                            sendResult = "发送中..."
                            sendResult = SendTask.send(subject, receivers, contents, attachmentFile)
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
                        placeholder = { Text("批量复制黏贴收件人, 分号分隔多个收件人.") },
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        onValueChange = { receivers = it }
                    )
                    OutlinedTextField(
                        value = contents,
                        label = { Text("正文") },
                        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 250.dp),
                        onValueChange = { contents = it }
                    )
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("附件: \t$attachmentPath")
                            Button(onClick = {
                                attachmentFile = null
                                attachmentPath = ""
                            }, modifier = Modifier.padding(start = 8.dp)) {
                                Text("删除")
                            }
                        }
                        OutlinedButton(onClick = {
                            val file = openFileDialog(window, title = "请选择附件", emptyList(), false)
                            attachmentFile = file.firstOrNull()
                            attachmentPath = attachmentFile?.absolutePath ?: ""
                        }, modifier = Modifier.padding(top = 8.dp)) {
                            Text("点击选择附件")
                        }
                    }
                }
                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .fillMaxHeight(),
                    adapter = rememberScrollbarAdapter(stateVertical)
                )
            }
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        icon = painterResource("app_icon.ico"),
        title = "WeiWeiMail",
        state = WindowState(position = WindowPosition.Aligned(Alignment.Center))
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
        App(window)
    }
}
