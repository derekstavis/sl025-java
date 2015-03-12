# encoding: utf-8

require 'pty'
require 'io/console'

SLEEP_TIME = (Float(ARGV[0]) rescue nil) || 2
DIST_JAR = "java -jar ../dist/sl025m-java.jar"

def send_response_serial(f)
  str = "\xBD\x0C\xF0\x00\x53\x4C\x30\x32\x35\x2D\x31\x2E\x32\x69"
  f.write str
end

def dead_process? pid
  begin
    puts "Checking #{pid}"
    Process.getpgid pid
  rescue Errno::ESRCH
    return true
  end

  return false
end


def just_do_it m, s
  pid = Process.spawn("#{DIST_JAR} -port #{s.path}")

  loop do

    begin

      sleep 2

      send_response_serial(m)
      puts "~> Serial Number\n"

      break if dead_process?(pid) 

    rescue SystemExit, Interrupt
      break
    rescue Exception => e
      puts e
    end

  end
end

loop do
  begin

    puts 'Waiting for connection'
    PTY.open do |m, s|
      m.raw!
      m.echo = false

      puts "Allocated master #{m}"
      puts "Slave path: #{s.path}"
      
      just_do_it m, s
      
    end

  sleep 1

  rescue SystemExit
    puts "Exiting graciously"
    exit 0
  rescue Interrupt
    puts 'To quit hit ctrl+c again. Any key to continue.'
    gets
    next
  end
end
