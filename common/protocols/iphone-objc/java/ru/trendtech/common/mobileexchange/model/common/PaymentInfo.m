#import "PaymentInfo.h"

@implementation PaymentInfo

@synthesize totalPrice;
@synthesize missionTime;
@synthesize pausesCount;
@synthesize pausesTime;
@synthesize distance;
@synthesize waitingOver;
@synthesize billStatus;
@synthesize comment;
@synthesize fixedPrice;
@synthesize services;

- (void) init {
  if (self = [super init]) {
    billStatus = 0;
    services = [[[NSMutableArray alloc] init] autorelease];
  }
  return self;
}

- (void) dealloc {
  [comment release];
  [services release];
  [super dealloc];
}

@end
