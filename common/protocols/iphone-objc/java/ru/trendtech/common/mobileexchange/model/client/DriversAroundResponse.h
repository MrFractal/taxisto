#import "ItemLocation.h"
#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 */

@interface DriversAroundResponse : NSObject {
  NSMutableArray * locations;
}

@property(nonatomic, retain, readonly) NSMutableArray * locations;
- (void) init;
@end
