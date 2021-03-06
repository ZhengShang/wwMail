// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

private lateinit var openSetting: () -> Unit

@OptIn(ExperimentalAnimationApi::class, DelicateCoroutinesApi::class)
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
                            sendResult = "?????????..."
                            GlobalScope.launch {
                                sendResult = SendTask.send(subject, receivers, contents, attachmentFile)
                            }
                        }) {
                        Text("Send~")
                    }
                    Text(text = sendResult, color = Color.Red)
                    OutlinedTextField(
                        value = subject,
                        label = { Text("??????") },
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { subject = it }
                    )
                    OutlinedTextField(
                        value = receivers,
                        label = { Text("?????????") },
                        placeholder = { Text("???????????????????????????, ????????????????????????????????????????????????.") },
                        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                        onValueChange = { receivers = it }
                    )
                    OutlinedTextField(
                        value = contents,
                        label = { Text("??????") },
                        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 250.dp),
                        onValueChange = { contents = it }
                    )
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("??????:  $attachmentPath")
                            AnimatedVisibility(
                                visible = attachmentFile != null
                            ) {
                                OutlinedButton(onClick = {
                                    attachmentFile = null
                                    attachmentPath = ""
                                }, modifier = Modifier.padding(start = 8.dp)) {
                                    Text("??????")
                                }
                            }
                        }
                        OutlinedButton(onClick = {
                            val file = openFileDialog(window, title = "???????????????", emptyList(), false)
                            attachmentFile = file.firstOrNull()
                            attachmentPath = attachmentFile?.absolutePath ?: ""
                        }, modifier = Modifier.padding(top = 8.dp)) {
                            Text("??????????????????")
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
        state = WindowState(position = WindowPosition.Aligned(Alignment.Center), size = WindowSize(900.dp, 700.dp))
    ) {
        MenuBar {
            Menu("??????", mnemonic = 'S') {
                Item("??????") {
                    AccountInfo.loadAccountInfo()
                    openSetting.invoke()
                }
                Separator()
                Item("??????", onClick = {

                })
            }
        }
        App(window)
    }
}
