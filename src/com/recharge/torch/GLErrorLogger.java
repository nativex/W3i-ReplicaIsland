/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.recharge.torch;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.opengl.GLU;


public class GLErrorLogger implements GLSurfaceView.GLWrapper {

	@Override
	public GL wrap(
			GL gl) {
		return new ErrorLoggingGL(gl);
	}

	static class ErrorLoggingGL implements GL, GL10, GL10Ext, GL11, GL11Ext {
		private GL mGL;

		public ErrorLoggingGL(GL gl) {
			mGL = gl;
		}

		public void checkError() {

			int error = ((GL10) mGL).glGetError();
			if (error != GL10.GL_NO_ERROR) {
				String method = Thread.currentThread().getStackTrace()[3].getMethodName();
				DebugLog.d("GL ERROR", "Error: " + error + " (" + GLU.gluErrorString(error) + "): " + method);
			}

			assert error == GL10.GL_NO_ERROR;
		}

		@Override
		public void glActiveTexture(
				int texture) {
			((GL10) mGL).glActiveTexture(texture);
			checkError();
		}

		@Override
		public void glAlphaFunc(
				int func,
				float ref) {
			((GL10) mGL).glAlphaFunc(func, ref);
			checkError();
		}

		@Override
		public void glAlphaFuncx(
				int func,
				int ref) {
			((GL10) mGL).glAlphaFuncx(func, ref);
			checkError();
		}

		@Override
		public void glBindTexture(
				int target,
				int texture) {
			((GL10) mGL).glBindTexture(target, texture);
			checkError();
		}

		@Override
		public void glBlendFunc(
				int sfactor,
				int dfactor) {
			((GL10) mGL).glBlendFunc(sfactor, dfactor);
			checkError();
		}

		@Override
		public void glClear(
				int mask) {
			((GL10) mGL).glClear(mask);
			checkError();
		}

		@Override
		public void glClearColor(
				float red,
				float green,
				float blue,
				float alpha) {
			((GL10) mGL).glClearColor(red, green, blue, alpha);
			checkError();
		}

		@Override
		public void glClearColorx(
				int red,
				int green,
				int blue,
				int alpha) {
			((GL10) mGL).glClearColorx(red, green, blue, alpha);
			checkError();
		}

		@Override
		public void glClearDepthf(
				float depth) {
			((GL10) mGL).glClearDepthf(depth);
			checkError();
		}

		@Override
		public void glClearDepthx(
				int depth) {
			((GL10) mGL).glClearDepthx(depth);
			checkError();
		}

		@Override
		public void glClearStencil(
				int s) {
			((GL10) mGL).glClearStencil(s);
			checkError();
		}

		@Override
		public void glClientActiveTexture(
				int texture) {
			((GL10) mGL).glClientActiveTexture(texture);
			checkError();
		}

		@Override
		public void glColor4f(
				float red,
				float green,
				float blue,
				float alpha) {
			((GL10) mGL).glColor4f(red, green, blue, alpha);
			checkError();
		}

		@Override
		public void glColor4x(
				int red,
				int green,
				int blue,
				int alpha) {
			((GL10) mGL).glColor4x(red, green, blue, alpha);
			checkError();
		}

		@Override
		public void glColorMask(
				boolean red,
				boolean green,
				boolean blue,
				boolean alpha) {
			((GL10) mGL).glColorMask(red, green, blue, alpha);
			checkError();
		}

		@Override
		public void glColorPointer(
				int size,
				int type,
				int stride,
				Buffer pointer) {
			((GL10) mGL).glColorPointer(size, type, stride, pointer);
			checkError();
		}

		@Override
		public void glCompressedTexImage2D(
				int target,
				int level,
				int internalformat,
				int width,
				int height,
				int border,
				int imageSize,
				Buffer data) {
			((GL10) mGL).glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
			checkError();
		}

		@Override
		public void glCompressedTexSubImage2D(
				int target,
				int level,
				int xoffset,
				int yoffset,
				int width,
				int height,
				int format,
				int imageSize,
				Buffer data) {
			((GL10) mGL).glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
			checkError();
		}

		@Override
		public void glCopyTexImage2D(
				int target,
				int level,
				int internalformat,
				int x,
				int y,
				int width,
				int height,
				int border) {
			((GL10) mGL).glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
			checkError();

		}

		@Override
		public void glCopyTexSubImage2D(
				int target,
				int level,
				int xoffset,
				int yoffset,
				int x,
				int y,
				int width,
				int height) {
			((GL10) mGL).glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
			checkError();
		}

		@Override
		public void glCullFace(
				int mode) {
			((GL10) mGL).glCullFace(mode);
			checkError();
		}

		@Override
		public void glDeleteTextures(
				int n,
				IntBuffer textures) {
			((GL10) mGL).glDeleteTextures(n, textures);
			checkError();
		}

		@Override
		public void glDeleteTextures(
				int n,
				int[] textures,
				int offset) {
			((GL10) mGL).glDeleteTextures(n, textures, offset);
			checkError();
		}

		@Override
		public void glDepthFunc(
				int func) {
			((GL10) mGL).glDepthFunc(func);
			checkError();
		}

		@Override
		public void glDepthMask(
				boolean flag) {
			((GL10) mGL).glDepthMask(flag);
			checkError();
		}

		@Override
		public void glDepthRangef(
				float zNear,
				float zFar) {
			((GL10) mGL).glDepthRangef(zNear, zFar);
			checkError();
		}

		@Override
		public void glDepthRangex(
				int zNear,
				int zFar) {
			((GL10) mGL).glDepthRangex(zNear, zFar);
			checkError();
		}

		@Override
		public void glDisable(
				int cap) {
			((GL10) mGL).glDisable(cap);
			checkError();
		}

		@Override
		public void glDisableClientState(
				int array) {
			((GL10) mGL).glDisableClientState(array);
			checkError();
		}

		@Override
		public void glDrawArrays(
				int mode,
				int first,
				int count) {
			((GL10) mGL).glDrawArrays(mode, first, count);
			checkError();
		}

		@Override
		public void glDrawElements(
				int mode,
				int count,
				int type,
				Buffer indices) {
			((GL10) mGL).glDrawElements(mode, count, type, indices);
			checkError();
		}

		@Override
		public void glEnable(
				int cap) {
			((GL10) mGL).glEnable(cap);
			checkError();
		}

		@Override
		public void glEnableClientState(
				int array) {
			((GL10) mGL).glEnableClientState(array);
			checkError();
		}

		@Override
		public void glFinish() {
			((GL10) mGL).glFinish();
			checkError();
		}

		@Override
		public void glFlush() {
			((GL10) mGL).glFlush();
			checkError();
		}

		@Override
		public void glFogf(
				int pname,
				float param) {
			((GL10) mGL).glFogf(pname, param);
			checkError();
		}

		@Override
		public void glFogfv(
				int pname,
				FloatBuffer params) {
			((GL10) mGL).glFogfv(pname, params);
			checkError();
		}

		@Override
		public void glFogfv(
				int pname,
				float[] params,
				int offset) {
			((GL10) mGL).glFogfv(pname, params, offset);
			checkError();
		}

		@Override
		public void glFogx(
				int pname,
				int param) {
			((GL10) mGL).glFogx(pname, param);
			checkError();
		}

		@Override
		public void glFogxv(
				int pname,
				IntBuffer params) {
			((GL10) mGL).glFogxv(pname, params);
			checkError();
		}

		@Override
		public void glFogxv(
				int pname,
				int[] params,
				int offset) {
			((GL10) mGL).glFogxv(pname, params, offset);
			checkError();
		}

		@Override
		public void glFrontFace(
				int mode) {
			((GL10) mGL).glFrontFace(mode);
			checkError();
		}

		@Override
		public void glFrustumf(
				float left,
				float right,
				float bottom,
				float top,
				float zNear,
				float zFar) {
			((GL10) mGL).glFrustumf(left, right, bottom, top, zNear, zFar);
			checkError();
		}

		@Override
		public void glFrustumx(
				int left,
				int right,
				int bottom,
				int top,
				int zNear,
				int zFar) {
			((GL10) mGL).glFrustumx(left, right, bottom, top, zNear, zFar);
			checkError();
		}

		@Override
		public void glGenTextures(
				int n,
				IntBuffer textures) {
			((GL10) mGL).glGenTextures(n, textures);
			checkError();
		}

		@Override
		public void glGenTextures(
				int n,
				int[] textures,
				int offset) {
			((GL10) mGL).glGenTextures(n, textures, offset);
			checkError();
		}

		@Override
		public int glGetError() {
			return ((GL10) mGL).glGetError();
		}

		@Override
		public void glGetIntegerv(
				int pname,
				IntBuffer params) {
			((GL10) mGL).glGetIntegerv(pname, params);
			checkError();
		}

		@Override
		public void glGetIntegerv(
				int pname,
				int[] params,
				int offset) {
			((GL10) mGL).glGetIntegerv(pname, params, offset);
			checkError();
		}

		@Override
		public String glGetString(
				int name) {
			String result = ((GL10) mGL).glGetString(name);
			checkError();
			return result;
		}

		@Override
		public void glHint(
				int target,
				int mode) {
			((GL10) mGL).glHint(target, mode);
			checkError();
		}

		@Override
		public void glLightModelf(
				int pname,
				float param) {
			((GL10) mGL).glLightModelf(pname, param);
			checkError();
		}

		@Override
		public void glLightModelfv(
				int pname,
				FloatBuffer params) {
			((GL10) mGL).glLightModelfv(pname, params);
			checkError();
		}

		@Override
		public void glLightModelfv(
				int pname,
				float[] params,
				int offset) {
			((GL10) mGL).glLightModelfv(pname, params, offset);
			checkError();
		}

		@Override
		public void glLightModelx(
				int pname,
				int param) {
			((GL10) mGL).glLightModelx(pname, param);
			checkError();
		}

		@Override
		public void glLightModelxv(
				int pname,
				IntBuffer params) {
			((GL10) mGL).glLightModelxv(pname, params);
			checkError();
		}

		@Override
		public void glLightModelxv(
				int pname,
				int[] params,
				int offset) {
			((GL10) mGL).glLightModelxv(pname, params, offset);
			checkError();
		}

		@Override
		public void glLightf(
				int light,
				int pname,
				float param) {
			((GL10) mGL).glLightf(light, pname, param);
			checkError();
		}

		@Override
		public void glLightfv(
				int light,
				int pname,
				FloatBuffer params) {
			((GL10) mGL).glLightfv(light, pname, params);
			checkError();
		}

		@Override
		public void glLightfv(
				int light,
				int pname,
				float[] params,
				int offset) {
			((GL10) mGL).glLightfv(light, pname, params, offset);
			checkError();
		}

		@Override
		public void glLightx(
				int light,
				int pname,
				int param) {
			((GL10) mGL).glLightx(light, pname, param);
			checkError();
		}

		@Override
		public void glLightxv(
				int light,
				int pname,
				IntBuffer params) {
			((GL10) mGL).glLightxv(light, pname, params);
			checkError();
		}

		@Override
		public void glLightxv(
				int light,
				int pname,
				int[] params,
				int offset) {
			((GL10) mGL).glLightxv(light, pname, params, offset);
			checkError();
		}

		@Override
		public void glLineWidth(
				float width) {
			((GL10) mGL).glLineWidth(width);
			checkError();
		}

		@Override
		public void glLineWidthx(
				int width) {
			((GL10) mGL).glLineWidthx(width);
			checkError();
		}

		@Override
		public void glLoadIdentity() {
			((GL10) mGL).glLoadIdentity();
			checkError();
		}

		@Override
		public void glLoadMatrixf(
				FloatBuffer m) {
			((GL10) mGL).glLoadMatrixf(m);
			checkError();
		}

		@Override
		public void glLoadMatrixf(
				float[] m,
				int offset) {
			((GL10) mGL).glLoadMatrixf(m, offset);
			checkError();
		}

		@Override
		public void glLoadMatrixx(
				IntBuffer m) {
			((GL10) mGL).glLoadMatrixx(m);
			checkError();
		}

		@Override
		public void glLoadMatrixx(
				int[] m,
				int offset) {
			((GL10) mGL).glLoadMatrixx(m, offset);
			checkError();
		}

		@Override
		public void glLogicOp(
				int opcode) {
			((GL10) mGL).glLogicOp(opcode);
			checkError();
		}

		@Override
		public void glMaterialf(
				int face,
				int pname,
				float param) {
			((GL10) mGL).glMaterialf(face, pname, param);
			checkError();
		}

		@Override
		public void glMaterialfv(
				int face,
				int pname,
				FloatBuffer params) {
			((GL10) mGL).glMaterialfv(face, pname, params);
			checkError();
		}

		@Override
		public void glMaterialfv(
				int face,
				int pname,
				float[] params,
				int offset) {
			((GL10) mGL).glMaterialfv(face, pname, params, offset);
			checkError();
		}

		@Override
		public void glMaterialx(
				int face,
				int pname,
				int param) {
			((GL10) mGL).glMaterialx(face, pname, param);
			checkError();
		}

		@Override
		public void glMaterialxv(
				int face,
				int pname,
				IntBuffer params) {
			((GL10) mGL).glMaterialxv(face, pname, params);
			checkError();
		}

		@Override
		public void glMaterialxv(
				int face,
				int pname,
				int[] params,
				int offset) {
			((GL10) mGL).glMaterialxv(face, pname, params, offset);
			checkError();
		}

		@Override
		public void glMatrixMode(
				int mode) {
			((GL10) mGL).glMatrixMode(mode);
			checkError();
		}

		@Override
		public void glMultMatrixf(
				FloatBuffer m) {
			((GL10) mGL).glMultMatrixf(m);
			checkError();
		}

		@Override
		public void glMultMatrixf(
				float[] m,
				int offset) {
			((GL10) mGL).glMultMatrixf(m, offset);
			checkError();
		}

		@Override
		public void glMultMatrixx(
				IntBuffer m) {
			((GL10) mGL).glMultMatrixx(m);
			checkError();
		}

		@Override
		public void glMultMatrixx(
				int[] m,
				int offset) {
			((GL10) mGL).glMultMatrixx(m, offset);
			checkError();
		}

		@Override
		public void glMultiTexCoord4f(
				int target,
				float s,
				float t,
				float r,
				float q) {
			((GL10) mGL).glMultiTexCoord4f(target, s, t, r, q);
			checkError();
		}

		@Override
		public void glMultiTexCoord4x(
				int target,
				int s,
				int t,
				int r,
				int q) {
			((GL10) mGL).glMultiTexCoord4x(target, s, t, r, q);
			checkError();
		}

		@Override
		public void glNormal3f(
				float nx,
				float ny,
				float nz) {
			((GL10) mGL).glNormal3f(nx, ny, nz);
			checkError();
		}

		@Override
		public void glNormal3x(
				int nx,
				int ny,
				int nz) {
			((GL10) mGL).glNormal3x(nx, ny, nz);
			checkError();
		}

		@Override
		public void glNormalPointer(
				int type,
				int stride,
				Buffer pointer) {
			((GL10) mGL).glNormalPointer(type, stride, pointer);
			checkError();
		}

		@Override
		public void glOrthof(
				float left,
				float right,
				float bottom,
				float top,
				float zNear,
				float zFar) {
			((GL10) mGL).glOrthof(left, right, bottom, top, zNear, zFar);
			checkError();
		}

		@Override
		public void glOrthox(
				int left,
				int right,
				int bottom,
				int top,
				int zNear,
				int zFar) {
			((GL10) mGL).glOrthox(left, right, bottom, top, zNear, zFar);
			checkError();
		}

		@Override
		public void glPixelStorei(
				int pname,
				int param) {
			((GL10) mGL).glPixelStorei(pname, param);
			checkError();
		}

		@Override
		public void glPointSize(
				float size) {
			((GL10) mGL).glPointSize(size);
			checkError();
		}

		@Override
		public void glPointSizex(
				int size) {
			((GL10) mGL).glPointSizex(size);
			checkError();
		}

		@Override
		public void glPolygonOffset(
				float factor,
				float units) {
			((GL10) mGL).glPolygonOffset(factor, units);
			checkError();
		}

		@Override
		public void glPolygonOffsetx(
				int factor,
				int units) {
			((GL10) mGL).glPolygonOffsetx(factor, units);
			checkError();
		}

		@Override
		public void glPopMatrix() {
			((GL10) mGL).glPopMatrix();
			checkError();
		}

		@Override
		public void glPushMatrix() {
			((GL10) mGL).glPushMatrix();
			checkError();
		}

		@Override
		public void glReadPixels(
				int x,
				int y,
				int width,
				int height,
				int format,
				int type,
				Buffer pixels) {
			((GL10) mGL).glReadPixels(x, y, width, height, format, type, pixels);
			checkError();
		}

		@Override
		public void glRotatef(
				float angle,
				float x,
				float y,
				float z) {
			((GL10) mGL).glRotatef(angle, x, y, z);
			checkError();
		}

		@Override
		public void glRotatex(
				int angle,
				int x,
				int y,
				int z) {
			((GL10) mGL).glRotatex(angle, x, y, z);
			checkError();
		}

		@Override
		public void glSampleCoverage(
				float value,
				boolean invert) {
			((GL10) mGL).glSampleCoverage(value, invert);
			checkError();
		}

		@Override
		public void glSampleCoveragex(
				int value,
				boolean invert) {
			((GL10) mGL).glSampleCoveragex(value, invert);
			checkError();
		}

		@Override
		public void glScalef(
				float x,
				float y,
				float z) {
			((GL10) mGL).glScalef(x, y, z);
			checkError();
		}

		@Override
		public void glScalex(
				int x,
				int y,
				int z) {
			((GL10) mGL).glScalex(x, y, z);
			checkError();
		}

		@Override
		public void glScissor(
				int x,
				int y,
				int width,
				int height) {
			((GL10) mGL).glScissor(x, y, width, height);
			checkError();
		}

		@Override
		public void glShadeModel(
				int mode) {
			((GL10) mGL).glShadeModel(mode);
			checkError();
		}

		@Override
		public void glStencilFunc(
				int func,
				int ref,
				int mask) {
			((GL10) mGL).glStencilFunc(func, ref, mask);
			checkError();
		}

		@Override
		public void glStencilMask(
				int mask) {
			((GL10) mGL).glStencilMask(mask);
			checkError();
		}

		@Override
		public void glStencilOp(
				int fail,
				int zfail,
				int zpass) {
			((GL10) mGL).glStencilOp(fail, zfail, zpass);
			checkError();
		}

		@Override
		public void glTexCoordPointer(
				int size,
				int type,
				int stride,
				Buffer pointer) {
			((GL10) mGL).glTexCoordPointer(size, type, stride, pointer);
			checkError();
		}

		@Override
		public void glTexEnvf(
				int target,
				int pname,
				float param) {
			((GL10) mGL).glTexEnvf(target, pname, param);
			checkError();
		}

		@Override
		public void glTexEnvfv(
				int target,
				int pname,
				FloatBuffer params) {
			((GL10) mGL).glTexEnvfv(target, pname, params);
			checkError();
		}

		@Override
		public void glTexEnvfv(
				int target,
				int pname,
				float[] params,
				int offset) {
			((GL10) mGL).glTexEnvfv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glTexEnvx(
				int target,
				int pname,
				int param) {
			((GL10) mGL).glTexEnvx(target, pname, param);
			checkError();
		}

		@Override
		public void glTexEnvxv(
				int target,
				int pname,
				IntBuffer params) {
			((GL10) mGL).glTexEnvxv(target, pname, params);
			checkError();
		}

		@Override
		public void glTexEnvxv(
				int target,
				int pname,
				int[] params,
				int offset) {
			((GL10) mGL).glTexEnvxv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glTexImage2D(
				int target,
				int level,
				int internalformat,
				int width,
				int height,
				int border,
				int format,
				int type,
				Buffer pixels) {
			((GL10) mGL).glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
			checkError();
		}

		@Override
		public void glTexParameterf(
				int target,
				int pname,
				float param) {
			((GL10) mGL).glTexParameterf(target, pname, param);
			checkError();
		}

		@Override
		public void glTexParameterx(
				int target,
				int pname,
				int param) {
			((GL10) mGL).glTexParameterx(target, pname, param);
			checkError();
		}

		@Override
		public void glTexSubImage2D(
				int target,
				int level,
				int xoffset,
				int yoffset,
				int width,
				int height,
				int format,
				int type,
				Buffer pixels) {
			((GL10) mGL).glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
			checkError();
		}

		@Override
		public void glTranslatef(
				float x,
				float y,
				float z) {
			((GL10) mGL).glTranslatef(x, y, z);
			checkError();
		}

		@Override
		public void glTranslatex(
				int x,
				int y,
				int z) {
			((GL10) mGL).glTranslatex(x, y, z);
			checkError();
		}

		@Override
		public void glVertexPointer(
				int size,
				int type,
				int stride,
				Buffer pointer) {
			((GL10) mGL).glVertexPointer(size, type, stride, pointer);
			checkError();
		}

		@Override
		public void glViewport(
				int x,
				int y,
				int width,
				int height) {
			((GL10) mGL).glViewport(x, y, width, height);
			checkError();
		}

		@Override
		public void glBindBuffer(
				int arg0,
				int arg1) {
			((GL11) mGL).glBindBuffer(arg0, arg1);
			checkError();
		}

		@Override
		public void glBufferData(
				int arg0,
				int arg1,
				Buffer arg2,
				int arg3) {
			((GL11) mGL).glBufferData(arg0, arg1, arg2, arg3);
			checkError();
		}

		@Override
		public void glBufferSubData(
				int arg0,
				int arg1,
				int arg2,
				Buffer arg3) {
			((GL11) mGL).glBufferSubData(arg0, arg1, arg2, arg3);
			checkError();
		}

		@Override
		public void glClipPlanef(
				int arg0,
				FloatBuffer arg1) {
			((GL11) mGL).glClipPlanef(arg0, arg1);
			checkError();
		}

		@Override
		public void glClipPlanef(
				int arg0,
				float[] arg1,
				int arg2) {
			((GL11) mGL).glClipPlanef(arg0, arg1, arg2);
			checkError();
		}

		@Override
		public void glClipPlanex(
				int arg0,
				IntBuffer arg1) {
			((GL11) mGL).glClipPlanex(arg0, arg1);
			checkError();
		}

		@Override
		public void glClipPlanex(
				int arg0,
				int[] arg1,
				int arg2) {
			((GL11) mGL).glClipPlanex(arg0, arg1, arg2);
			checkError();
		}

		@Override
		public void glColor4ub(
				byte arg0,
				byte arg1,
				byte arg2,
				byte arg3) {
			((GL11) mGL).glColor4ub(arg0, arg1, arg2, arg3);
			checkError();
		}

		@Override
		public void glColorPointer(
				int arg0,
				int arg1,
				int arg2,
				int arg3) {
			((GL11) mGL).glColorPointer(arg0, arg1, arg2, arg3);
			checkError();
		}

		@Override
		public void glDeleteBuffers(
				int n,
				IntBuffer buffers) {
			((GL11) mGL).glDeleteBuffers(n, buffers);
			checkError();
		}

		@Override
		public void glDeleteBuffers(
				int n,
				int[] buffers,
				int offset) {
			((GL11) mGL).glDeleteBuffers(n, buffers, offset);
			checkError();
		}

		@Override
		public void glDrawElements(
				int mode,
				int count,
				int type,
				int offset) {
			((GL11) mGL).glDrawElements(mode, count, type, offset);
			checkError();
		}

		@Override
		public void glGenBuffers(
				int n,
				IntBuffer buffers) {
			((GL11) mGL).glGenBuffers(n, buffers);
			checkError();
		}

		@Override
		public void glGenBuffers(
				int n,
				int[] buffers,
				int offset) {
			((GL11) mGL).glGenBuffers(n, buffers, offset);
			checkError();
		}

		@Override
		public void glGetBooleanv(
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetBooleanv(pname, params);
			checkError();
		}

		@Override
		public void glGetBooleanv(
				int pname,
				boolean[] params,
				int offset) {
			((GL11) mGL).glGetBooleanv(pname, params, offset);
			checkError();
		}

		@Override
		public void glGetBufferParameteriv(
				int target,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetBufferParameteriv(target, pname, params);
			checkError();
		}

		@Override
		public void glGetBufferParameteriv(
				int target,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetBufferParameteriv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetClipPlanef(
				int pname,
				FloatBuffer eqn) {
			((GL11) mGL).glGetClipPlanef(pname, eqn);
			checkError();
		}

		@Override
		public void glGetClipPlanef(
				int pname,
				float[] eqn,
				int offset) {
			((GL11) mGL).glGetClipPlanef(pname, eqn, offset);
			checkError();
		}

		@Override
		public void glGetClipPlanex(
				int pname,
				IntBuffer eqn) {
			((GL11) mGL).glGetClipPlanex(pname, eqn);
			checkError();
		}

		@Override
		public void glGetClipPlanex(
				int pname,
				int[] eqn,
				int offset) {
			((GL11) mGL).glGetClipPlanex(pname, eqn, offset);
			checkError();
		}

		@Override
		public void glGetFixedv(
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetFixedv(pname, params);
			checkError();
		}

		@Override
		public void glGetFixedv(
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetFixedv(pname, params, offset);
			checkError();
		}

		@Override
		public void glGetFloatv(
				int pname,
				FloatBuffer params) {
			((GL11) mGL).glGetFloatv(pname, params);
			checkError();
		}

		@Override
		public void glGetFloatv(
				int pname,
				float[] params,
				int offset) {
			((GL11) mGL).glGetFloatv(pname, params, offset);
			checkError();
		}

		@Override
		public void glGetLightfv(
				int light,
				int pname,
				FloatBuffer params) {
			((GL11) mGL).glGetLightfv(light, pname, params);
			checkError();
		}

		@Override
		public void glGetLightfv(
				int light,
				int pname,
				float[] params,
				int offset) {
			((GL11) mGL).glGetLightfv(light, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetLightxv(
				int light,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetLightxv(light, pname, params);
			checkError();
		}

		@Override
		public void glGetLightxv(
				int light,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetLightxv(light, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetMaterialfv(
				int face,
				int pname,
				FloatBuffer params) {
			((GL11) mGL).glGetMaterialfv(face, pname, params);
			checkError();
		}

		@Override
		public void glGetMaterialfv(
				int face,
				int pname,
				float[] params,
				int offset) {
			((GL11) mGL).glGetMaterialfv(face, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetMaterialxv(
				int face,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetMaterialxv(face, pname, params);
			checkError();
		}

		@Override
		public void glGetMaterialxv(
				int face,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetMaterialxv(face, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetPointerv(
				int pname,
				Buffer[] params) {
			((GL11) mGL).glGetPointerv(pname, params);
			checkError();
		}

		@Override
		public void glGetTexEnviv(
				int env,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetTexEnviv(env, pname, params);
			checkError();
		}

		@Override
		public void glGetTexEnviv(
				int env,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetTexEnviv(env, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetTexEnvxv(
				int env,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetTexEnvxv(env, pname, params);
			checkError();
		}

		@Override
		public void glGetTexEnvxv(
				int env,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetTexEnvxv(env, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetTexParameterfv(
				int target,
				int pname,
				FloatBuffer params) {
			((GL11) mGL).glGetTexParameterfv(target, pname, params);
			checkError();
		}

		@Override
		public void glGetTexParameterfv(
				int target,
				int pname,
				float[] params,
				int offset) {
			((GL11) mGL).glGetTexParameterfv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetTexParameteriv(
				int target,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetTexParameteriv(target, pname, params);
			checkError();
		}

		@Override
		public void glGetTexParameteriv(
				int target,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetTexParameteriv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glGetTexParameterxv(
				int target,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glGetTexParameterxv(target, pname, params);
			checkError();
		}

		@Override
		public void glGetTexParameterxv(
				int target,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glGetTexParameterxv(target, pname, params, offset);
			checkError();
		}

		@Override
		public boolean glIsBuffer(
				int buffer) {
			boolean result = ((GL11) mGL).glIsBuffer(buffer);
			checkError();
			return result;
		}

		@Override
		public boolean glIsEnabled(
				int cap) {
			boolean result = ((GL11) mGL).glIsEnabled(cap);
			checkError();
			return result;
		}

		@Override
		public boolean glIsTexture(
				int texture) {
			boolean result = ((GL11) mGL).glIsTexture(texture);
			checkError();
			return result;
		}

		@Override
		public void glNormalPointer(
				int type,
				int stride,
				int offset) {
			((GL11) mGL).glNormalPointer(type, stride, offset);
			checkError();
		}

		@Override
		public void glPointParameterf(
				int pname,
				float param) {
			((GL11) mGL).glPointParameterf(pname, param);
			checkError();
		}

		@Override
		public void glPointParameterfv(
				int pname,
				FloatBuffer params) {
			((GL11) mGL).glPointParameterfv(pname, params);
			checkError();
		}

		@Override
		public void glPointParameterfv(
				int pname,
				float[] params,
				int offset) {
			((GL11) mGL).glPointParameterfv(pname, params, offset);
			checkError();
		}

		@Override
		public void glPointParameterx(
				int pname,
				int param) {
			((GL11) mGL).glPointParameterx(pname, param);
			checkError();
		}

		@Override
		public void glPointParameterxv(
				int pname,
				IntBuffer params) {
			((GL11) mGL).glPointParameterxv(pname, params);
			checkError();
		}

		@Override
		public void glPointParameterxv(
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glPointParameterxv(pname, params, offset);
			checkError();
		}

		@Override
		public void glPointSizePointerOES(
				int type,
				int stride,
				Buffer pointer) {
			((GL11) mGL).glPointSizePointerOES(type, stride, pointer);
			checkError();
		}

		@Override
		public void glTexCoordPointer(
				int size,
				int type,
				int stride,
				int offset) {
			((GL11) mGL).glTexCoordPointer(size, type, stride, offset);
			checkError();
		}

		@Override
		public void glTexEnvi(
				int target,
				int pname,
				int param) {
			((GL11) mGL).glTexEnvi(target, pname, param);
			checkError();
		}

		@Override
		public void glTexEnviv(
				int target,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glTexEnviv(target, pname, params);
			checkError();
		}

		@Override
		public void glTexEnviv(
				int target,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glTexEnviv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glTexParameterfv(
				int target,
				int pname,
				FloatBuffer params) {
			((GL11) mGL).glTexParameterfv(target, pname, params);
			checkError();
		}

		@Override
		public void glTexParameterfv(
				int target,
				int pname,
				float[] params,
				int offset) {
			((GL11) mGL).glTexParameterfv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glTexParameteri(
				int target,
				int pname,
				int param) {
			((GL11) mGL).glTexParameteri(target, pname, param);
			checkError();
		}

		@Override
		public void glTexParameteriv(
				int target,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glTexParameteriv(target, pname, params);
			checkError();
		}

		@Override
		public void glTexParameteriv(
				int target,
				int pname,
				int[] params,
				int offset) {
		}

		@Override
		public void glTexParameterxv(
				int target,
				int pname,
				IntBuffer params) {
			((GL11) mGL).glTexParameterxv(target, pname, params);
			checkError();
		}

		@Override
		public void glTexParameterxv(
				int target,
				int pname,
				int[] params,
				int offset) {
			((GL11) mGL).glTexParameterxv(target, pname, params, offset);
			checkError();
		}

		@Override
		public void glVertexPointer(
				int size,
				int type,
				int stride,
				int offset) {
			((GL11) mGL).glVertexPointer(size, type, stride, offset);
			checkError();
		}

		@Override
		public void glCurrentPaletteMatrixOES(
				int matrixpaletteindex) {
			((GL11Ext) mGL).glCurrentPaletteMatrixOES(matrixpaletteindex);
			checkError();
		}

		@Override
		public void glDrawTexfOES(
				float x,
				float y,
				float z,
				float width,
				float height) {
			((GL11Ext) mGL).glDrawTexfOES(x, y, z, width, height);
			checkError();
		}

		@Override
		public void glDrawTexfvOES(
				FloatBuffer coords) {
			((GL11Ext) mGL).glDrawTexfvOES(coords);
			checkError();
		}

		@Override
		public void glDrawTexfvOES(
				float[] coords,
				int offset) {
			((GL11Ext) mGL).glDrawTexfvOES(coords, offset);
			checkError();
		}

		@Override
		public void glDrawTexiOES(
				int x,
				int y,
				int z,
				int width,
				int height) {
			((GL11Ext) mGL).glDrawTexiOES(x, y, z, width, height);
			checkError();
		}

		@Override
		public void glDrawTexivOES(
				IntBuffer coords) {
			((GL11Ext) mGL).glDrawTexivOES(coords);
			checkError();

		}

		@Override
		public void glDrawTexivOES(
				int[] coords,
				int offset) {
			((GL11Ext) mGL).glDrawTexivOES(coords, offset);
			checkError();

		}

		@Override
		public void glDrawTexsOES(
				short x,
				short y,
				short z,
				short width,
				short height) {
			((GL11Ext) mGL).glDrawTexsOES(x, y, z, width, height);
			checkError();
		}

		@Override
		public void glDrawTexsvOES(
				ShortBuffer coords) {
			((GL11Ext) mGL).glDrawTexsvOES(coords);
			checkError();

		}

		@Override
		public void glDrawTexsvOES(
				short[] coords,
				int offset) {
			((GL11Ext) mGL).glDrawTexsvOES(coords, offset);
			checkError();

		}

		@Override
		public void glDrawTexxOES(
				int x,
				int y,
				int z,
				int width,
				int height) {
			((GL11Ext) mGL).glDrawTexxOES(x, y, z, width, height);
			checkError();
		}

		@Override
		public void glDrawTexxvOES(
				IntBuffer coords) {
			((GL11Ext) mGL).glDrawTexxvOES(coords);
			checkError();

		}

		@Override
		public void glDrawTexxvOES(
				int[] coords,
				int offset) {
			((GL11Ext) mGL).glDrawTexxvOES(coords, offset);
			checkError();

		}

		@Override
		public void glLoadPaletteFromModelViewMatrixOES() {
			((GL11Ext) mGL).glLoadPaletteFromModelViewMatrixOES();
			checkError();

		}

		@Override
		public void glMatrixIndexPointerOES(
				int size,
				int type,
				int stride,
				Buffer pointer) {
			((GL11Ext) mGL).glMatrixIndexPointerOES(size, type, stride, pointer);
			checkError();

		}

		@Override
		public void glMatrixIndexPointerOES(
				int size,
				int type,
				int stride,
				int offset) {
			((GL11Ext) mGL).glMatrixIndexPointerOES(size, type, stride, offset);
			checkError();
		}

		@Override
		public void glWeightPointerOES(
				int size,
				int type,
				int stride,
				Buffer pointer) {
			((GL11Ext) mGL).glWeightPointerOES(size, type, stride, pointer);
			checkError();
		}

		@Override
		public void glWeightPointerOES(
				int size,
				int type,
				int stride,
				int offset) {
			((GL11Ext) mGL).glWeightPointerOES(size, type, stride, offset);
			checkError();
		}

		@Override
		public int glQueryMatrixxOES(
				IntBuffer arg0,
				IntBuffer arg1) {
			int result = ((GL10Ext) mGL).glQueryMatrixxOES(arg0, arg1);
			checkError();
			return result;
		}

		@Override
		public int glQueryMatrixxOES(
				int[] arg0,
				int arg1,
				int[] arg2,
				int arg3) {
			int result = ((GL10Ext) mGL).glQueryMatrixxOES(arg0, arg1, arg2, arg3);
			checkError();
			return result;
		}
	}
}
