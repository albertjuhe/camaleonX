/*
 * Copyright (c) 2010, 2013, Fados-productions S.L. , omaonk and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of  Fados-productions S.L. or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * You may contact the author at [albertjuhe@gmail.com]
 * And the copyright holder at [albertjuhe@gmail.com] [Ramón turró 23 - 08005 Barcelona]
 */


package omaonk.utils;

/**
 * 
 * @author ajuhe
 */
public class elapsed {

	long timeMs = 0;
	String seconds = "00";
	String minutes = "00";
	String hours = "00";
	String mms = "000";

	public elapsed(long timems) {
		this.timeMs = timems;
		this.calculate();
	}

	private void calculate() {
		long timeMillis = this.timeMs;
		long time = timeMillis / 1000;
		this.seconds = Integer.toString((int) (time % 60));
		this.minutes = Integer.toString((int) ((time % 3600) / 60));
		this.hours = Integer.toString((int) (time / 3600));
		long miliseconds = timeMillis - (Integer.parseInt(hours) * 3600000)
				- (Integer.parseInt(minutes) * 60000)
				- (Integer.parseInt(seconds) * 1000);
		this.mms = Integer.toString((int) miliseconds);

		for (int i = 0; i < 2; i++) {
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			if (minutes.length() < 2) {
				minutes = "0" + minutes;
			}
			if (hours.length() < 2) {
				hours = "0" + hours;
			}
		}
	}

	public String toString() {
		return hours + "h " + minutes + "m " + seconds + "s " + mms + "ms";
	}
}
