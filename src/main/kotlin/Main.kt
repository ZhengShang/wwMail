// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application


@Composable
@Preview
fun App() {
    var subject by remember { mutableStateOf("") }
    var receivers by remember { mutableStateOf("") }
    var contents by remember { mutableStateOf("") }

    DesktopMaterialTheme {
        Row {
            OutlinedTextField(
                value = subject,
                label = { Text("标题") },
                onValueChange = { subject = it }
            )
        }
        Row {
            OutlinedTextField(
                value = receivers,
                label = { Text("收件人") },
                onValueChange = { receivers = it }
            )
        }
        Row {
            OutlinedTextField(
                value = contents,
                label = { Text("正文") },
                onValueChange = { contents = it }
            )
        }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "WeiWeiMail"
    ) {
        App()
    }
}
