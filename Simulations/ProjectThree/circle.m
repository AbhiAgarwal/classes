% circular blocks, cars not in order

clear all
close all

global dmin dmax vmax;

% initialization
nblocks = 10; 
L = 1; % mile  % L>dmax

% R = 0.5, p = 0.5 --> 2 SS
% R = 0.56, p = 0.5 --> 2 SS
% R = 1.3, p = 0.5 --> 2 very close SS
% R = 1.25 (x = 0.015, y = 0.0375), 1 (x=0.06, y=0.15)
% R = 1.2, p = 0.5 --> 2 maybe close (x=0.0225, y=0.054) 1 (x=0.0675, y =0.162) 2
% R = 0.925, p = 0.35 (x = 0.0225, y = 0.05946) 1, (x = 0.0525, y = 0.1388) 2
 
% mile/min
dmin = 0.01; % mile 
dmax = 0.5;   % mile  % dmax<L
%vmax = (R/p) * dmin * log(dmax/dmin) * 2.73;
vmax = 20/60;

% 1.06468; and 0.34
% 0.5
% 5.0

%R = 0.78234; % prob/minuet of entry 
%p = 0.34; % probability of exit when passing each exit

R = 5.0;
p = 0.34;

% Mean dist = total length / number of cars
% plot point (mean_dist, v(mean_dist))
% on the curve? good predictor?

dt = dmin / vmax * 0.5;
tmax = 150;
clockmax = ceil(tmax / dt);
tsave = zeros(1, clockmax);
Nsave = zeros(1, clockmax);  % # of cars on road
v_average = zeros(1, clockmax);
d_average = zeros(1, clockmax);

% % empty road initially
N = 0; count = 0;
x = [];  % position of car c, x range [0, nblocks*L) 
nextcar = []; % index of next car behind c on its block (0 if c is lastcar)
firstcar = zeros(1, nblocks);
lastcar = zeros(1, nblocks);
tenter = [];
texit = [];

x_plot = linspace(0, dmax * 1.5, 101);
[m, q] = intersections(x_plot, vcar(x_plot), x_plot, R / p * x_plot, 0);

% We use the
% floor function on the total length of the roundabout divided by m, which
% is Number of blocks * L /x of intersection One. 
% The explaination of this is we can say m is the unstable point, and
% length of roundable
% m would give us a value for the equadistant length between each
% car.


% 2.800 <-> 2.825
% 2.8125 went up
% 2.815 went down

% narrow it further up
% increase it little more up

% g = ((m(3) - 2.81375*m(2)) / 2);
% 
% % % N uniform distributed cars at beginning
% N = floor((nblocks*L)/(g)); 
% count = N;
% x = zeros(1, N) * nblocks * L;
% 
% x = linspace(0, nblocks * L-m(2), N); 
% 
% x = sort(x,'descend');
% for b = 1:nblocks
%     ind = find(x < b * L&x >= (b-1) * L);
%     if isempty(ind)
%         firstcar(b) = 0; lastcar(b) = 0;
%     else
%         firstcar(b) = ind(1); lastcar(b) = ind(end);
%     end
% end
% nextcar = 2:N; nextcar(lastcar) = 0;
% tenter = zeros(1,N); texit = [];

figure(1);
set(gcf, 'double', 'on')
radius = nblocks * L / (2 * pi);
theta = x / radius;
h1 = scatter(radius * cos(theta), radius * sin(theta), 50 * ones(size(x)), 'fill');
axis([-radius * 1.5 radius * 1.5 -radius * 1.5 radius * 1.5])
axis equal 
axis manual 
t = 0;
h2=title(sprintf('time=%0.2f min, total # of cars=%d', t, N));
hold on
% viscircles([0 0],radius,'EdgeColor','k');
theta = linspace(0, 2 * pi, nblocks + 1);
scatter(radius * cos(theta), radius * sin(theta), 100 * ones(1, nblocks + 1), 'r'); % exits

for clock = 1:clockmax
    t = clock * dt;
    tsave(clock) = t;
    % entry of cars
    for b = 1:nblocks
        if rand < R * dt
            count = count + 1; % car index
            if (lastcar(b) == 0) % if the block is empty
                firstcar(b) = count;
                lastcar(b) = count;
                nextcar(count) = 0;
            else
                nextcar(lastcar(b)) = count;
                nextcar(count) = 0;
                lastcar(b) = count;
            end
            x(count) = (b-1) * L;
            tenter(count) = t;
        end
    end
    % motion and exit of cars
    for b = 1:nblocks 
        bnext = b + 1 - nblocks * (b == nblocks); % circular blocks
        if firstcar(b) ~= 0  % if block is not empty
            c = firstcar(b);
            if lastcar(bnext) == 0
                d = dmax;
            else
                d = x(lastcar(bnext)) - x(c) + nblocks * L * (bnext == 1); % when near x=0
            end
            x(c) = x(c) + dt * vcar(d);
            if x(c) > b * L  % if car passed end of block b
                if (firstcar(b) == lastcar(b))
                    firstcar(b) = 0;
                    lastcar(b) = 0;
                else
                    firstcar(b) = nextcar(c);
                end
                nextcar(c) = 0;
                if rand < p % did the car exit?
                    texit(c) = t;
                    x(c) = -1;
                else
                    if lastcar(bnext) == 0
                        firstcar(bnext) = c;
                        lastcar(bnext) = c;
                    else 
                        nextcar(lastcar(bnext)) = c;
                        lastcar(bnext) = c;
                    end
                    x(c) = x(c) - (bnext == 1) * nblocks * L;
                end
            end
            cp = c; % trailing pointer
            c = nextcar(c);
            while c ~= 0
                x(c) = x(c) + dt * vcar(x(cp) - x(c));
                cp = c;
                c = nextcar(c);
            end
        end
    end
    % average d and v
    Nsave(clock) = length(tenter) - nnz(texit);
    ind_onroad = setdiff(1:count, find(texit)); % index of cars that are on road
    x_sort = sort(x(ind_onroad));
    if isempty(x_sort) == 0
        d = [diff(x_sort), x_sort(1) + nblocks * L - x_sort(end)];
        d_average(clock) = mean(d);
        v_average(clock) = mean(vcar(d));
    end
    
    theta = (x(x>0) / radius);

    set(h1, 'xdata', radius * cos(theta), 'ydata', radius * sin(theta), 'sizedata', 50 * ones(size(theta)))
    set(h2,'string', sprintf('time=%0.2f min, total # of cars=%d', t, Nsave(clock)))
    drawnow
    
    figure(4)
    d_plot = linspace(0, dmax * 1.5, 101);
    plot(d_plot, vcar(d_plot), d_plot, R / p * d_plot)
    text(d_average(clock), v_average(clock), strcat('\diamondsuit ', '  (',num2str(d_average(clock)),', ', num2str(v_average(clock)), ')'))
    axis([0 dmax * 1.5 0 vmax * 1.5])
    
end

figure(2)
plot(tsave, Nsave)
       
figure(3)
plot(tsave, d_average, tsave, v_average)

% percentage error
100 * (abs(v_average(clockmax) - vcar(d_average(clockmax)))/(v_average(clockmax)))