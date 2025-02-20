package com.example.modapjt.toktok.gesture

import android.gesture.Gesture
import android.gesture.GesturePoint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun GestureDrawingCanvas(
    onGestureRecognized: (Pair<String, Double>?) -> Unit
) {
    val context = LocalContext.current
    val gestureManager = remember { GestureManager(context) }

    // 현재 그리고 있는 경로를 저장하는 Path
    var currentPath by remember { mutableStateOf(Path()) }
    // 여러 개의 스트로크(선)을 저장하는 리스트
    var currentStrokes by remember { mutableStateOf(mutableListOf<List<GesturePoint>>()) }
    // 현재 그리고 있는 한 개의 스트로크를 저장하는 리스트
    var currentStroke by remember { mutableStateOf(mutableListOf<GesturePoint>()) }
    // 현재 그리기 중인지 상태 저장
    var isDrawing by remember { mutableStateOf(false) }

    // 이전 터치 포인트 저장 (부드러운 곡선을 그리기 위해 사용)
    var previousPoint by remember { mutableStateOf<Offset?>(null) }

    // 경로와 생성 시간을 함께 저장
    data class TimedPath(
        val path: Path,
        val createdAt: Long
    )

    // 시간 정보가 포함된 경로들을 저장하는 리스트
    var pathsWithTimestamp by remember { mutableStateOf(listOf<TimedPath>()) }

    // 애니메이션을 위한 시간 틱 상태
    var animationTick by remember { mutableStateOf(0L) }

    // 애니메이션 틱을 주기적으로 업데이트하는 코루틴
    LaunchedEffect(Unit) {
        while (true) {
            delay(16) // 약 60fps
            animationTick = System.currentTimeMillis()
        }
    }

    // 시스템 다크모드 감지
    val isDarkTheme = isSystemInDarkTheme()

    // 다크모드에 따른 색상 설정
    val lineColor = Color(0xFFFFFFFF)
    val lineWidth = 30f

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        // 드래그 시작시
                        onDragStart = { offset ->
                            isDrawing = true
                            // 이전 데이터 초기화
                            currentStroke.clear()
                            currentStrokes.clear()
                            // 새로운 경로 시작
                            currentPath = Path().apply {
                                moveTo(offset.x, offset.y)
                            }
                            // 첫 포인트 추가
                            currentStroke.add(GesturePoint(offset.x, offset.y, System.currentTimeMillis()))
                            previousPoint = null
                        },
                        // 드래그 종료시
                        onDragEnd = {
                            isDrawing = false
                            // 최소 2개 이상의 포인트가 있을 때만 처리
                            if (currentStroke.size > 1) {
                                currentStrokes.add(currentStroke.toList())
                                // 제스처 객체 생성
                                val gesture = Gesture().apply {
                                    currentStrokes.forEach { points ->
                                        addStroke(android.gesture.GestureStroke(points as ArrayList<GesturePoint>))
                                    }
                                }

                                // 제스처가 의미있는 크기인 경우에만 처리
                                val bounds = gesture.boundingBox
                                if (abs(bounds.width()) > 20 && abs(bounds.height()) > 20) {
                                    val recognized = gestureManager.recognizeGesture(gesture)
                                    onGestureRecognized(recognized)
                                }
                            }

                            // 현재 경로를 타임스탬프와 함께 저장
                            pathsWithTimestamp = pathsWithTimestamp + TimedPath(
                                currentPath,
                                System.currentTimeMillis()
                            )

                            // 상태 초기화
                            currentStroke = mutableListOf()
                            currentPath = Path()
                        },
                        // 드래그 중
                        onDrag = { change, _ ->
                            val newPoint = change.position

                            // 이전 점이 없으면 시작점으로 설정
                            // 부드러운 곡선을 그리기 위한 로직
                            if (previousPoint == null) {
                                previousPoint = newPoint
                                currentPath.moveTo(newPoint.x, newPoint.y)
                            } else {
                                // 현재 점과 이전 점 사이의 중간점 계산
                                val midPoint = Offset(
                                    (previousPoint!!.x + newPoint.x) / 2f,
                                    (previousPoint!!.y + newPoint.y) / 2f
                                )

                                // 이전 중간점에서 현재 중간점까지 곡선으로 연결
                                // 베지어 곡선으로 선 그리기
                                currentPath.quadraticBezierTo(
                                    previousPoint!!.x, previousPoint!!.y, // 제어점
                                    midPoint.x, midPoint.y               // 도착점
                                )

                                previousPoint = newPoint
                            }

                            // 포인트 저장
                            currentStroke.add(GesturePoint(newPoint.x, newPoint.y, System.currentTimeMillis()))
                        }
                    )
                }
        ) {
            val currentTime = animationTick

            // 2초 이상 된 경로는 제거
            pathsWithTimestamp = pathsWithTimestamp.filter {
                currentTime - it.createdAt < 2000
            }

            // 저장된 경로들 그리기 (fade out 효과 적용)
            pathsWithTimestamp.forEach { timedPath ->
                val age = currentTime - timedPath.createdAt
                val alpha = 1f - (age / 500f)  // 2초에 걸쳐 fade out

                drawPath(
                    path = timedPath.path,
                    color = lineColor.copy(alpha = alpha.coerceIn(0f, 1f)),
                    style = Stroke(
                        width = lineWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // 현재 그리고 있는 선 그리기
            drawPath(
                path = currentPath,
                color = lineColor,
                style = Stroke(
                    width = lineWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}